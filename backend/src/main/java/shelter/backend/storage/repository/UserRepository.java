package shelter.backend.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shelter.backend.rest.model.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);

    void deleteUserByEmail(String email);

    List<User> findAllByShelterNameIsNotNullAndIsDisabledIsTrue();

    List<User> findAllByShelterNameIsNotNull();
}
