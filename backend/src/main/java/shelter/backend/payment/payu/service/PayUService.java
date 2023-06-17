package shelter.backend.payment.payu.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import shelter.backend.adoption.service.VirtualAdoptionService;
import shelter.backend.configuration.ContextDelegateAware;
import shelter.backend.email.EmailService;
import shelter.backend.payment.payu.configuration.PayUConfigurationProperties;
import shelter.backend.payment.payu.rest.model.Buyer;
import shelter.backend.payment.payu.rest.model.Product;
import shelter.backend.payment.payu.rest.model.req.OrderCreateRequest;
import shelter.backend.payment.payu.rest.model.req.OrderDataRequest;
import shelter.backend.payment.payu.rest.model.req.OrderRetrieveRequest;
import shelter.backend.payment.payu.rest.model.res.Order;
import shelter.backend.payment.payu.rest.model.res.OrderCreateResponse;
import shelter.backend.payment.payu.rest.model.res.OrderRetrieveResponse;
import shelter.backend.payment.payu.rest.model.res.OrderStatus;
import shelter.backend.payment.payu.rest.model.res.PayUAuthToken;
import shelter.backend.payment.payu.rest.model.res.Status;
import shelter.backend.rest.model.entity.PayUClientCredentials;
import shelter.backend.rest.model.entity.PaymentOrder;
import shelter.backend.rest.model.entity.Token;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.Purpose;
import shelter.backend.storage.repository.PayUClientCredentialsRepository;
import shelter.backend.storage.repository.PaymentOrderRepository;
import shelter.backend.utils.constants.ShelterConstants;
import shelter.backend.utils.exception.PaymentException;

import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayUService implements PaymentService {

    private final PayUClientCredentialsRepository payUClientCredentialsRepository;

    private final PaymentOrderRepository paymentOrderRepository;

    private final PayUConfigurationProperties payUConfigurationProperties;

    private final PaymentAuthorizationService paymentAuthorizationService;

    private final RestTemplate restTemplate;

    private final OrderDataRequest orderDataRequest;

    private final ContextDelegateAware contextDelegateAware;

    private final EmailService emailService;

    @Value("${payment.payu.validUntil}")
    private Long expiresIn;

    @Value("${server.port}")
    private int serverPort;

    @Override
    public String commencePayment(User user, User shelter) {
        log.info("Commencing ordering payment for the user: {}, amount: {}, shelter: {}", user, shelter, orderDataRequest.getAmount());
        PayUClientCredentials payUClientCredentials = payUClientCredentialsRepository.findByShelter_Id(shelter.getId())
                .orElseThrow(() -> new EntityNotFoundException("Nie istnieje schronisko dla wybranego id"));
        PayUAuthToken payUAuthToken = getAccessToken(payUClientCredentials);
        return makeOrder(user, shelter, payUClientCredentials, payUAuthToken);
    }

    @Override
    public OrderStatus checkPaymentStatus(String extOrderId, Optional<String> error) {
        log.debug("Checking payment status for extOrderId: {}", extOrderId);
        PaymentOrder order = paymentOrderRepository.findByExtOrderId(String.valueOf(extOrderId));
        if (order == null) {
            log.error("Cannot load payment order for extOrderId: {}", extOrderId);
            throw new PaymentException("Problem z płatnością");
        }
        log.info("PaymentOrder loaded successfully, {}", order);
        if (error.isEmpty()) {
            emailService.sendPaymentInfo(order.getUserName(), order.getPurpose().getPurposeName(),
                    String.valueOf(order.getAmount()/100), order.getShelterName(), order.getId());
        }
        return callForPaymentStatus(order, false);
    }

    private OrderStatus callForPaymentStatus(PaymentOrder order, boolean isRepeated) { //todo rm this dummy isRepeated
        PayUClientCredentials payUClientCredentials = payUClientCredentialsRepository.findByShelter_Id(Long.parseLong(order.getShelterId()))
                .orElseThrow(() -> new EntityNotFoundException("Nie istnieje schronisko dla wybranego id"));
        PayUAuthToken payUAuthToken = getAccessToken(payUClientCredentials);
        OrderStatus status = retrieveOrderStatus(order.getId(), payUAuthToken);
        if (status == null) {
            log.error("Cannot retrieve payment status for payment order: {}", order);
            throw new PaymentException("Nie można sprawdzić statusu płatności");
        }
        log.info("Payment status for payment order {} -> {}", order, status);

        handlePayment(status, order, isRepeated);
        return status;
    }

    @Override
    public Boolean updatePaymentOrderWithEntityServiceId(Long id) {
        log.debug("Updating payment order with entity service ID: {}. OrderDataRequest: {}", id, orderDataRequest);
        PaymentOrder paymentOrder = paymentOrderRepository.findByExtOrderId(orderDataRequest.getExtOrderId());
        paymentOrder.setEntityServiceId(String.valueOf(id));
        paymentOrderRepository.save(paymentOrder);
        return true;
    }

    private void handlePayment(OrderStatus status, PaymentOrder paymentOrder, boolean isRepeated) { //todo rm this dummy isRepeated
        switch (status) {
            case CANCELED -> {
                log.info("Payment has been canceled, processing canceled payment flow.");
                handleCanceledPayment(paymentOrder);
                emailService.sendPaymentFailure(paymentOrder.getUserName(), paymentOrder.getPurpose().name(),
                        String.valueOf(paymentOrder.getAmount()), paymentOrder.getShelterName(), paymentOrder.getId());
            }
            case COMPLETED -> {
                log.info("Payment confirmed and completed, processing successful payment flow.");
                handleCompletedPayment(paymentOrder); //register adoption, delete payment order
            }
            case PENDING -> { //fixme/todo remove this, use notification system provided by payu. just for presentation purpose
                if (!isRepeated) {
                    Executor executor = CompletableFuture.delayedExecutor(10000L, TimeUnit.MILLISECONDS); //after 10 secs create new thread(async) and check the status
                    executor.execute(() -> callForPaymentStatus(paymentOrder, true));
                }
            }
        }
    }

    private void handleCompletedPayment(PaymentOrder paymentOrder) {
        switch (paymentOrder.getPurpose()) {
            case VIRTUAL_ADOPTION -> {
                VirtualAdoptionService virtualAdoptionService = contextDelegateAware.getService("shelterVirtualAdoptionSerivce", VirtualAdoptionService.class);
                virtualAdoptionService.finalizeVirtualAdoption(Long.parseLong(paymentOrder.getEntityServiceId()), paymentOrder.getAmount());
            }
        }
        paymentOrderRepository.deleteById(paymentOrder.getId());
    }

    private void handleCanceledPayment(PaymentOrder paymentOrder) {
        switch (paymentOrder.getPurpose()) {
            case VIRTUAL_ADOPTION -> {
                VirtualAdoptionService virtualAdoptionService = contextDelegateAware.getService("shelterVirtualAdoptionSerivce", VirtualAdoptionService.class);
                virtualAdoptionService.delete(Long.parseLong(paymentOrder.getEntityServiceId()));
            }
        }
        paymentOrderRepository.deleteById(paymentOrder.getId());
    }


    private PayUAuthToken getAccessToken(PayUClientCredentials payUClientCredentials) {
        UriComponents authorizationRequest = prepareAuthorizationRequest(payUClientCredentials);
        PayUAuthToken payUAuthToken = paymentAuthorizationService.authorize(authorizationRequest);
        log.info("Access token retrieved successfully for shelter : {}", payUClientCredentials.getShelter().getShelterName());
        return payUAuthToken;
    }

    private String makeOrder(User user, User shelter, PayUClientCredentials payUClientCredentials, PayUAuthToken payUAuthToken) {
        final OrderCreateRequest orderRequest = prepareOrderCreateRequest(payUClientCredentials);

        log.info("Order request = {}", orderRequest);

        final OrderCreateResponse orderResponse = sendOrder(orderRequest, payUAuthToken);

        log.info("Order response = {}", orderResponse);

        if (!orderResponse.getStatus().getStatusCode().equals(Status.STATUS_CODE_SUCCESS)) {
            log.warn("Payment failed during ordering. UserId: {}, ShelterId: {}", user.getId(), shelter.getId());
            throw new PaymentException("Płatność zakończona niepowodzeniem");
        }
        PaymentOrder paymentOrder = new PaymentOrder(orderResponse.getOrderId(), orderResponse.getExtOrderId(),
                user.getEmail(), shelter.getId().toString(), shelter.getShelterName(), orderDataRequest.getAmount(),
                Purpose.VIRTUAL_ADOPTION, null, expiresIn.intValue());
        paymentOrderRepository.save(paymentOrder);
        orderDataRequest.setExtOrderId(orderResponse.getExtOrderId());

        log.debug("Payment order stored. {}", paymentOrder);
        return orderResponse.getRedirectUri();
    }

    private OrderStatus retrieveOrderStatus(String orderId, PayUAuthToken payUAuthToken) {
        OrderRetrieveRequest orderRetrieveRequest = new OrderRetrieveRequest();
        orderRetrieveRequest.setOrderId(orderId);
        OrderRetrieveResponse orderRetrieveResponse = getOrderStatus(orderRetrieveRequest, payUAuthToken);
        if (orderRetrieveResponse != null && orderRetrieveResponse.getOrders() != null && !orderRetrieveResponse.getOrders().isEmpty()) {
            return orderRetrieveResponse.getOrders().stream().filter(order -> order.getOrderId().equals(orderId))
                    .map(Order::getStatus).findFirst().orElse(null);
        } else {
            return null;
        }
    }

    private UriComponents prepareAuthorizationRequest(PayUClientCredentials payUClientCredentials) {
        MultiValueMap<String, String> queryParamMap = new LinkedMultiValueMap<>();
        queryParamMap.add(ShelterConstants.CLIENT_ID, payUClientCredentials.getClientId());
        queryParamMap.add(ShelterConstants.CLIENT_SECRET, payUClientCredentials.getClientSecret());
        queryParamMap.add(ShelterConstants.GRANT_TYPE, payUConfigurationProperties.getGrantType());
        return UriComponentsBuilder.fromUriString(payUConfigurationProperties.getAuthorizationUri())
                .queryParams(queryParamMap)
                .build();
    }

    private OrderCreateRequest prepareOrderCreateRequest(PayUClientCredentials payUClientCredentials) {

        final String serverAddress = "http://localhost:" + serverPort;
        final String extOrderId = UUID.randomUUID().toString();
        final String callBackUrl = StringSubstitutor.replace(payUConfigurationProperties.getCallbackPath(),
                Map.of("extOrderId", extOrderId), "{", "}");

        return OrderCreateRequest.builder()
                .extOrderId(extOrderId)
                .customerIp(orderDataRequest.getIpAddress())
                .continueUrl(serverAddress + callBackUrl)
//                .notifyUrl(serverAddress + payUConfigurationProperties.getOrderNotificationUrl())//fixme/todo
                .merchantPosId(payUClientCredentials.getMerchantPosId())
                .description(orderDataRequest.getDescription())
                .currencyCode(orderDataRequest.getCurrency())
                .totalAmount(String.valueOf(orderDataRequest.getAmount()))
                .products(Collections.singletonList(Product.builder()
                        .name(orderDataRequest.getName())
                        .quantity(orderDataRequest.getQuantity())
                        .unitPrice(orderDataRequest.getUnitPrice())
                        .build()))
                .buyer(Buyer.builder()
                        .email(orderDataRequest.getBuyerEmail())
                        .language(ShelterConstants.ORDER_LANG)
                        .build())
                .build();
    }

    private OrderCreateResponse sendOrder(OrderCreateRequest orderRequest, PayUAuthToken payUAuthToken) {
        HttpHeaders headers = getHttpHeaders(payUAuthToken);
        HttpEntity<OrderCreateRequest> entity = new HttpEntity<>(orderRequest, headers);
        ResponseEntity<OrderCreateResponse> responseEntity = restTemplate.postForEntity(payUConfigurationProperties.getOrderUrl(), entity, OrderCreateResponse.class);
        return responseEntity.getBody();
    }

    private OrderRetrieveResponse getOrderStatus(OrderRetrieveRequest orderRetrieveRequest, PayUAuthToken payUAuthToken) {
        log.debug("[getOrderStatus] :: retrieving order for orderId: {}", orderRetrieveRequest.getOrderId());

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(payUConfigurationProperties.getOrderCheckStatusUrl());
        URI endpointUrl = builder.buildAndExpand(orderRetrieveRequest.getOrderId()).toUri();
        HttpHeaders headers = getHttpHeaders(payUAuthToken);
        HttpEntity<OrderRetrieveRequest> entity = new HttpEntity<>(headers);
        ResponseEntity<OrderRetrieveResponse> responseEntity = restTemplate.exchange(
                endpointUrl, HttpMethod.GET, entity, OrderRetrieveResponse.class);
        log.info("[getOrderStatus] :: RESPONSE: {}", responseEntity);
        return responseEntity.getBody();
    }

    private HttpHeaders getHttpHeaders(PayUAuthToken payUAuthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, payUAuthToken.getTokenType() + " " + payUAuthToken.getAccessToken());
        return headers;
    }

    @Scheduled(fixedDelay = 7200000)   //check every 2 hours //fixme/todo use notification system provided by payu
    public void callForPaymentStatus() {
        log.debug("payu scheduler started");
        Iterable<PaymentOrder> paymentOrders = paymentOrderRepository.findAll();
        for (PaymentOrder paymentOrder : paymentOrders) {
            callForPaymentStatus(paymentOrder, true);
        }
        log.debug("payu scheduler finished");
    }
}

