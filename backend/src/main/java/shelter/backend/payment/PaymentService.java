package shelter.backend.payment;

import shelter.backend.rest.model.entity.User;

public interface PaymentService {

    void proccessPayment(long amount, User shelter);

}
