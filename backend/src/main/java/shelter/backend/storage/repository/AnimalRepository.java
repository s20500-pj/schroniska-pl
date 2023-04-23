package shelter.backend.storage.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shelter.backend.rest.model.entity.Animal;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnimalRepository extends CrudRepository<Animal, Long>, JpaSpecificationExecutor<Animal> {

    Animal findAnimalById(Long id);

    @Query("SELECT a FROM Animal a LEFT JOIN a.activities act WHERE act.activityTime <> :date OR act.id IS NULL")
    List<Animal> findAllAnimalsWithoutActivityAtDate(@Param("date") LocalDateTime date);
}
