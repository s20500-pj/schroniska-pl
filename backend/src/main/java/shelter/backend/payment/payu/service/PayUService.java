package shelter.backend.payment.payu.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import shelter.backend.adoption.service.VirtualAdoptionService;
import shelter.backend.payment.PaymentService;
import shelter.backend.payment.payu.configuration.PayUConfigurationProperties;
import shelter.backend.payment.payu.rest.model.PayUAuthToken;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.PayUClientCredentials;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.Purpose;
import shelter.backend.storage.repository.PayUClientCredentialsRepository;
import shelter.backend.storage.repository.PaymentOrderRepository;
import shelter.backend.utils.constants.ShelterConstants;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayUService implements PaymentService {

    private final PayUClientCredentialsRepository payUClientCredentialsRepository;

    private final PaymentOrderRepository paymentOrderRepository;

    private final PayUConfigurationProperties payUConfigurationProperties;

    private final PaymentAuthorizationService paymentAuthorizationService;

    private final RestTemplate restTemplate;

    @Lazy
    private final VirtualAdoptionService virtualAdoptionService;

    @Override
    public String commencePayment(long amount, User user, Animal animal, Purpose purpose) {
        User shelter = animal.getShelter();
        log.info("Commencing ordering payment for the user: {}, amount: {}, shelter: {}", user, shelter, amount);
        PayUClientCredentials payUClientCredentials = payUClientCredentialsRepository.findById(shelter.getId())
                .orElseThrow(() -> new EntityNotFoundException("Nie istnieje schronisko dla wybranego id"));
        String accessToken = getAccessToken(payUClientCredentials);
        return makeOrder(user, shelter, amount, purpose, accessToken);
    }

    private String getAccessToken(PayUClientCredentials payUClientCredentials) {
        UriComponents authorizationRequest = prepareAuthorizationRequest(payUClientCredentials);
        PayUAuthToken payUAuthToken = paymentAuthorizationService.authorize(authorizationRequest);
        log.info("Access token retrieved successfully for shelter : {}", payUClientCredentials.getShelter().getShelterName());
        return payUAuthToken.getAccessToken();
    }

    private String makeOrder(User user, User shelter, long amount, Purpose purpose, String accessToken) {
//        final OrderCreateRequest orderRequest = prepareOrderCreateRequest(payUForm, request);
//
//        log.info("Order request = {}", orderRequest);
//
//        final OrderCreateResponse orderResponse = orderService.order(orderRequest);
//
//        if (!orderResponse.getStatus().getStatusCode().equals(STATUS_CODE_SUCCESS)) {
//            throw new RuntimeException("Payment failed! ");
//        }
//
//        return new orderResponse.getRedirectUri();
        // TODO: 16/04/2023
        return null;
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

}
