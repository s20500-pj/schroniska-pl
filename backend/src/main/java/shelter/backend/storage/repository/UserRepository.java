package shelter.backend.storage.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shelter.backend.rest.model.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findUserByEmail(String email);

    void deleteUserByEmail(String email);

    List<User> findAllByShelterNameIsNotNullAndIsDisabledIsTrue();

    List<User> findAllByShelterNameIsNotNull();

    User findUserById(Long id);
}
