package shelter.backend.payment;

import shelter.backend.rest.model.entity.Animal;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.Purpose;

public interface PaymentService {

    String commencePayment(User user, Animal animal);

}
