package taxi.project.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import taxi.project.demo.entities.Client;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.entities.Order;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByDriver_Id(Long driverId);

    List<Order> findAllByClient_Id(Long clientId);

    @Modifying
    @Transactional
    @Query("update Order o set o.driver = ?1 where o.id = ?2")
    void changeDriver(Driver driver, Long orderId);

    @Modifying
    @Transactional
    @Query("update Order o set o.client = ?1 where o.id = ?2")
    void changeClient(Client client, Long orderId);

    @Modifying
    @Transactional
    @Query("update Order o set o.price = ?1 where o.id = ?2")
    void changePrice(Double price, Long orderId);

}
