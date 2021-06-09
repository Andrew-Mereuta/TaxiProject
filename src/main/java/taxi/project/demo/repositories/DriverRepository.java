package taxi.project.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taxi.project.demo.entities.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
}
