package ma.elbouchouki.digitalbanking.security.repositories;


import ma.elbouchouki.digitalbanking.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);

    AppUser findByEmail(String email);
}
