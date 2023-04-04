package shelter.backend.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import shelter.backend.rest.model.entity.Adoption;
import shelter.backend.rest.model.entity.Animal;

import java.util.List;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long>, JpaSpecificationExecutor<Adoption> {
    List<Adoption> findAdoptionByUserId(Long userId);
    List<Adoption> findAdoptionByAnimal_ShelterId(Long shelterID);
}
