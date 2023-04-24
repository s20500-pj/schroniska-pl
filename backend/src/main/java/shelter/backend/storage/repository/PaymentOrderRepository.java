package shelter.backend.storage.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shelter.backend.rest.model.entity.PaymentOrder;

import java.util.List;
@Repository
public interface PaymentOrderRepository extends CrudRepository<PaymentOrder, String> {
    PaymentOrder findByExtOrderId(String extOrderId);
}
