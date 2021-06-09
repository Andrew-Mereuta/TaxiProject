package taxi.project.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taxi.project.demo.entities.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
