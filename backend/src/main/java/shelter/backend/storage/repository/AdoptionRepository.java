package shelter.backend.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import shelter.backend.rest.model.entity.Adoption;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.AdoptionType;

import java.util.List;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long>, JpaSpecificationExecutor<Adoption> {
    List<Adoption> findAdoptionByUserId(Long userId);

    List<Adoption> findAdoptionByAnimal_ShelterIdAndAdoptionType(Long shelterID, AdoptionType adoptionType);

    List<Adoption> findAdoptionByUserIdAndAdoptionType(Long userId, AdoptionType adoptionType);

    List<Adoption> findAdoptionByAdoptionType(AdoptionType adoptionType);

    List<Adoption> findByUser(User user);
}
