package shelter.backend.payment.payu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shelter.backend.payment.PaymentService;
import shelter.backend.rest.model.entity.User;

@Service
@RequiredArgsConstructor
public class PayUService implements PaymentService {

    @Override
    public void proccessPayment(long amount, User shelter) {
        //save order ID and shelter to REDIS. setup TTL, go on with the process payu
    }
}
