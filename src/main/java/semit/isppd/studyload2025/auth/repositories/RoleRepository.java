package semit.isppd.studyload2025.auth.repositories;

import semit.isppd.studyload2025.auth.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role,Long> {
    @Query(value = "SELECT r FROM Role r WHERE r.name=:name")
    Role getRoleByName(String name);
}
