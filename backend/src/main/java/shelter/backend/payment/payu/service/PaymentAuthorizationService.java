package shelter.backend.payment.payu.service;

import org.springframework.web.util.UriComponents;
import shelter.backend.payment.payu.rest.model.PayUAuthToken;

public interface PaymentAuthorizationService {
    PayUAuthToken authorize(UriComponents authenticationDetails);
}
