package ma.elbouchouki.digitalbanking.repository;

import ma.elbouchouki.digitalbanking.model.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface OperationRepository extends JpaRepository<Operation, String> {
    Set<Operation> findAllByBankAccountId(String bankAccountId);

}
