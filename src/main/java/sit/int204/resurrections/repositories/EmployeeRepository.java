package sit.int204.resurrections.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int204.resurrections.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
