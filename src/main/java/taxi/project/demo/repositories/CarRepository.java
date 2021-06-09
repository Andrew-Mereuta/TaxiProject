package taxi.project.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taxi.project.demo.entities.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
