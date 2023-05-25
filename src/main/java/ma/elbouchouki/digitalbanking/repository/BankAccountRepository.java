package ma.elbouchouki.digitalbanking.repository;

import ma.elbouchouki.digitalbanking.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

}