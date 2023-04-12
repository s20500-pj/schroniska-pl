package shelter.backend.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import shelter.backend.rest.model.entity.Activity;
import shelter.backend.rest.model.entity.Adoption;
import shelter.backend.rest.model.enums.ActivityType;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long>, JpaSpecificationExecutor<Activity> {
    List<Activity> findActivitiesByAnimal_ShelterId(Long userId);
}
