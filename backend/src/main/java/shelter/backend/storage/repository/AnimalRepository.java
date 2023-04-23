package shelter.backend.storage.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shelter.backend.rest.model.entity.Animal;

@Repository
public interface AnimalRepository extends CrudRepository<Animal, Long>, JpaSpecificationExecutor<Animal> {
    Animal findAnimalById(Long id);
}
