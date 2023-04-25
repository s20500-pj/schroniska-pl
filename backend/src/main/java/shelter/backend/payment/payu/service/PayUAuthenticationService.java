package shelter.backend.payment.payu.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import shelter.backend.payment.payu.rest.model.res.PayUAuthToken;
import shelter.backend.utils.constants.ShelterConstants;
import shelter.backend.utils.exception.PaymentException;

@Service
@Slf4j
@AllArgsConstructor
public class PayUAuthenticationService implements PaymentAuthorizationService {

    private final RestTemplate restTemplate;

    @Override
    public PayUAuthToken authorize(UriComponents authorizationRequest) {
        final ResponseEntity<PayUAuthToken> response = restTemplate.postForEntity(authorizationRequest.toUri(),
                null, PayUAuthToken.class);
        if (response.getStatusCode().isError()) {
            log.error("Problem with retrieving the access token for clientId: {}. Response: {}",
                    authorizationRequest.getQueryParams().getFirst(ShelterConstants.CLIENT_ID), response);
            throw new PaymentException("Nie można zautoryzować płatności PayU");
        }
        return response.getBody();
    }
}
