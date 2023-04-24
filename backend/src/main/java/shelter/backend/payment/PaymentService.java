package shelter.backend.payment;

import shelter.backend.payment.payu.rest.model.res.OrderStatus;
import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.User;

public interface PaymentService {

    String commencePayment(User user, Animal animal);

    OrderStatus checkPaymentStatus(Long extOrderId);
}
