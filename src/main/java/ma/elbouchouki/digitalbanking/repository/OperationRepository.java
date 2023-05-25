package ma.elbouchouki.digitalbanking.repository;

import ma.elbouchouki.digitalbanking.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation, String> {

}
