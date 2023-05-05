package shelter.backend.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import shelter.backend.rest.model.entity.Activity;
import shelter.backend.rest.model.entity.Adoption;
import shelter.backend.rest.model.entity.User;
import shelter.backend.rest.model.enums.ActivityType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long>, JpaSpecificationExecutor<Activity> {
    List<Activity> findActivitiesByAnimal_ShelterId(Long shelterId);

    List<Activity> findActivitiesByActivityTime(LocalDateTime dateTime);

    List<Activity> findActivitiesByActivityTimeAndUserId(LocalDateTime dateTime, Long userId);

    List<Activity> findActivitiesByUserId(Long userId);

    List<Activity> findByUser(User user);
}
