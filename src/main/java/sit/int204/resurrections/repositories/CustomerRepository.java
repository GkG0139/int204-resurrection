package sit.int204.resurrections.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int204.resurrections.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
