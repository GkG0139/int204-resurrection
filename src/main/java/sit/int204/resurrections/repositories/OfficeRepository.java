package sit.int204.resurrections.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int204.resurrections.entities.Office;

import java.util.List;

public interface OfficeRepository extends JpaRepository<Office, String> {
    List<Office> findByCityContaining(String city);
}
