package shelter.backend.payment.payu.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import shelter.backend.payment.PaymentService;
import shelter.backend.payment.payu.configuration.PayUConfigurationProperties;
import shelter.backend.payment.payu.rest.model.Buyer;
import shelter.backend.payment.payu.rest.model.OrderCreateRequest;
import shelter.backend.payment.payu.rest.model.OrderCreateResponse;
import shelter.backend.payment.payu.rest.model.OrderDataRequest;
import shelter.backend.payment.payu.rest.model.PayUAuthToken;
import shelter.backend.payment.payu.rest.model.Product;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.PayUClientCredentials;
import shelter.backend.rest.model.entity.PaymentOrder;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.Purpose;
import shelter.backend.storage.repository.PayUClientCredentialsRepository;
import shelter.backend.storage.repository.PaymentOrderRepository;
import shelter.backend.utils.constants.ShelterConstants;
import shelter.backend.utils.exception.PaymentException;

import java.util.Collections;
import java.util.UUID;

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

    @Value("${server.port}")
    private int serverPort;

    @Override
    public String commencePayment(User user, Animal animal) {
        User shelter = animal.getShelter();
        log.info("Commencing ordering payment for the user: {}, amount: {}, shelter: {}", user, shelter, orderDataRequest.getAmount());
        PayUClientCredentials payUClientCredentials = payUClientCredentialsRepository.findById(shelter.getId())
                .orElseThrow(() -> new EntityNotFoundException("Nie istnieje schronisko dla wybranego id"));
        PayUAuthToken payUAuthToken = getAccessToken(payUClientCredentials);
        return makeOrder(user, shelter, payUClientCredentials, payUAuthToken);
    }

    private PayUAuthToken getAccessToken(PayUClientCredentials payUClientCredentials) {
        UriComponents authorizationRequest = prepareAuthorizationRequest(payUClientCredentials);
        PayUAuthToken payUAuthToken = paymentAuthorizationService.authorize(authorizationRequest);
        log.info("Access token retrieved successfully for shelter : {}", payUClientCredentials.getShelter().getShelterName());
        return payUAuthToken;
    }

    private String makeOrder(User user, User shelter, PayUClientCredentials payUClientCredentials, PayUAuthToken payUAuthToken) {
        final OrderCreateRequest orderRequest = prepareOrderCreateRequest(payUClientCredentials, user.getId());

        log.info("Order request = {}", orderRequest);

        final OrderCreateResponse orderResponse = sendOrder(orderRequest, payUAuthToken);

        if (!orderResponse.getStatus().getStatusCode().equals(OrderCreateResponse.Status.STATUS_CODE_SUCCESS)) {
            log.warn("Payment failed during ordering. UserId: {}, ShelterId: {}", user.getId(), shelter.getId());
            throw new PaymentException("Płatność zakończona niepowodzeniem");
        }
        PaymentOrder paymentOrder = new PaymentOrder(orderResponse.getOrderId(), orderResponse.getExtOrderId(),
                user.getEmail(), shelter.getShelterName(), orderDataRequest.getAmount(), Purpose.VIRTUAL_ADOPTION);
        paymentOrderRepository.save(paymentOrder); //TODO test the repo custom method

        return orderResponse.getRedirectUri();
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

    private OrderCreateRequest prepareOrderCreateRequest(PayUClientCredentials payUClientCredentials, Long id) {

        final String serverAddress = "http://localhost:" + serverPort;
        StringBuilder builder = new StringBuilder();
        if (id != null) {
            builder.append("/").append(id);
        }
        final String pathVariable = builder.toString();

        return OrderCreateRequest.builder()
                .extOrderId(UUID.randomUUID().toString())
                .customerIp(orderDataRequest.getIpAddress())
                .continueUrl(serverAddress + payUConfigurationProperties.getCallbackPath() + pathVariable)
//                .notifyUrl(serverAddress + payUConfigurationProperties.getNotifyPath())
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
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, payUAuthToken.getTokenType() + " " + payUAuthToken.getAccessToken());

        HttpEntity<OrderCreateRequest> entity = new HttpEntity<>(orderRequest, headers);

        ResponseEntity<OrderCreateResponse> responseEntity = restTemplate.postForEntity(payUConfigurationProperties.getOrderUrl(), entity, OrderCreateResponse.class);

        return responseEntity.getBody();
    }

}

