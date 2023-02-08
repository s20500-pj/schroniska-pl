package shelter.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shelter.backend.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
