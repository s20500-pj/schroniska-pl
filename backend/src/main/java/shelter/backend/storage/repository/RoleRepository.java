package shelter.backend.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shelter.backend.rest.model.entity.Role;
import shelter.backend.rest.model.enums.ERole;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(ERole role);
}
