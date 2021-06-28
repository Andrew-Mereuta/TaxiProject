package taxi.project.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taxi.project.demo.entities.Client;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.entities.Order;
import taxi.project.demo.repositories.OrderRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public int saveOrder(Order order) {
        orderRepository.save(order);
        return 201;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getAllOrdersOfDriver(Long driverId) {
        return orderRepository.findAllByDriver_Id(driverId);
    }

    public List<Order> getAllOrdersOfClient(Long clientId) {
        return orderRepository.findAllByClient_Id(clientId);
    }

    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        order.setDriver(null);
        order.setClient(null);
        orderRepository.save(order);
        orderRepository.delete(order);
    }

    public void changeDriver(Order order, Driver driver) {
        orderRepository.changeDriver(driver, order.getId());
        order.setDriver(driver);
    }

    public void changeClient(Order order, Client client) {
        orderRepository.changeClient(client, order.getId());
        order.setClient(client);
    }

    public void changePrice(Order order, Double price) {
        orderRepository.changePrice(price, order.getId());
        order.setPrice(price);
    }

    public boolean updateOrder(Long orderId, Order order, Client client, Driver driver) {
        Optional<Order> o = orderRepository.findById(orderId);
        if(o.isEmpty()) {
            return false;
        }
        changeDriver(o.get(), driver);
        changeClient(o.get(), client);
        changePrice(o.get(), order.getPrice());
        return true;
    }
}
