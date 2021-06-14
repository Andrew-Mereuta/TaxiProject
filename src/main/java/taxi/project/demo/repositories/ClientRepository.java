package taxi.project.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import taxi.project.demo.entities.Client;

import javax.transaction.Transactional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByEmail(String email);

    @Modifying
    @Transactional
    @Query("update Client c set c.email = ?1, c.name = ?2, c.password = ?3 where c.id = ?4")
    void updateClient(String email, String name, String password, Long clientId);
}
