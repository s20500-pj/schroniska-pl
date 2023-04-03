package shelter.backend.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shelter.backend.rest.model.entity.Adoption;

import java.util.List;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
    List<Adoption> findAdoptionByUserId(Long userId);
    List<Adoption> findAdoptionByAnimal_ShelterId(Long shelterID);
}
