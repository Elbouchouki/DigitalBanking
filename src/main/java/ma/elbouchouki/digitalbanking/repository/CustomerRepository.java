package ma.elbouchouki.digitalbanking.repository;

import ma.elbouchouki.digitalbanking.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

}
