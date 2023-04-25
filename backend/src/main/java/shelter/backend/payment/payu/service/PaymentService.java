package shelter.backend.payment.payu.service;

import shelter.backend.payment.payu.rest.model.res.OrderStatus;
import shelter.backend.rest.model.entity.User;

import java.util.Optional;

public interface PaymentService {

    String commencePayment(User user, User shelter);

    OrderStatus checkPaymentStatus(String extOrderId, Optional<String> error);

    Boolean updatePaymentOrderWithEntityServiceId(Long id);
}
