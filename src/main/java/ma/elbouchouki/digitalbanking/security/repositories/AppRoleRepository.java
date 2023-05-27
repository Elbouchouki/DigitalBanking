package ma.elbouchouki.digitalbanking.security.repositories;

import ma.elbouchouki.digitalbanking.security.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    AppRole findByRoleName(String roleName);

    List<AppRole> findAll();
}
