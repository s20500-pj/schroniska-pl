package shelter.backend.storage.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shelter.backend.rest.model.entity.PayUClientCredentials;
import shelter.backend.rest.model.entity.Token;

import java.util.Optional;

@Repository
public interface PayUClientCredentialsRepository extends CrudRepository<PayUClientCredentials, Long> {
    Optional<PayUClientCredentials> findByShelter_Id(Long shelterId);
}
