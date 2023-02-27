package shelter.backend.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shelter.backend.rest.model.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
