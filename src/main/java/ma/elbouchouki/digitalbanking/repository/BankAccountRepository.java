package ma.elbouchouki.digitalbanking.repository;

import ma.elbouchouki.digitalbanking.model.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    Set<BankAccount> findAllByCustomerId(String customerId);
}
