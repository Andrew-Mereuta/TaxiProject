package taxi.project.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import taxi.project.demo.entities.Client;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.entities.Order;
import taxi.project.demo.repositories.OrderRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    private final OrderService orderService = new OrderService(orderRepository);

    private List<Order> orders = new ArrayList<>();
    private Client originalClient = new Client();
    private Driver originalDriver = new Driver();

    @BeforeEach
    void setUp() {
        originalClient.setName("client");
        originalClient.setPassword("pass");
        originalClient.setRole("ROLE_CLIENT");
        originalClient.setEmail("client@email.com");
        originalClient.setId(1L);

        originalDriver.setName("driver");
        originalDriver.setPassword("password");
        originalDriver.setRole("ROLE_DRIVER");
        originalDriver.setEmail("driver@email.com");
        originalDriver.setId(1L);

        orders.add(new Order(1L, originalClient, originalDriver, 74.2));
        orders.add(new Order(2L, originalClient, originalDriver, 82.8));
        orders.add(new Order(3L, originalClient, originalDriver, 71.3));
    }

    @Test
    public void saveOrderTest() {
        assertEquals(201, orderService.saveOrder(orders.get(0)));
        verify(orderRepository, times(1)).save(orders.get(0));
    }

    @Test
    public void getALLOrdersTest() {
        when(orderRepository.findAll()).thenReturn(orders);
        assertArrayEquals(orders.toArray(), orderService.getAllOrders().toArray());
    }

    @Test
    public void getAllOrdersOfDriverTest() {
        when(orderRepository.findAllByDriver_Id(1L)).thenReturn(orders);
        assertArrayEquals(orders.toArray(), orderService.getAllOrdersOfDriver(1L).toArray());
    }

    @Test
    public void getAllOrdersOfClientTest() {
        when(orderRepository.findAllByClient_Id(1L)).thenReturn(orders);
        assertArrayEquals(orders.toArray(), orderService.getAllOrdersOfClient(1L).toArray());
    }

    @Test
    public void findOrderByIdTest1() {
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(orders.get(0)));
        assertEquals(orders.get(0), orderService.findOrderById(1L));
    }

    @Test
    public void findOrderByIdTest2() {
        when(orderRepository.findById(4L)).thenReturn(java.util.Optional.empty());
        assertNull( orderService.findOrderById(4L));
    }

    @Test
    public void deleteOrderTest() {
        orderService.deleteOrder(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    public void changeDriverTest() {
        Driver d = new Driver();
        d.setName("dDriver");
        d.setBusy(false);
        d.setPassword("passwordd");
        d.setEmail("driver@mail.ru");
        d.setId(2L);
        orderService.changeDriver(orders.get(0), d);
        verify(orderRepository, times(1)).changeDriver(d, orders.get(0).getId());
    }

    @Test
    public void changeClientTest() {
        Client c = new Client();
        c.setName("cClient");
        c.setPassword("passwordc");
        c.setEmail("cclient@mail.ru");
        c.setId(2L);
        orderService.changeClient(orders.get(0), c);
        verify(orderRepository, times(1)).changeClient(c, orders.get(0).getId());
    }

    @Test
    public void changePriceTest() {
        orderService.changePrice(orders.get(0), 69.71);
        verify(orderRepository, times(1)).changePrice(69.71, orders.get(0).getId());
    }

    @Test
    public void updateOrderTest1() {
        Driver d = new Driver();
        d.setName("dDriver");
        d.setBusy(false);
        d.setPassword("passwordd");
        d.setEmail("driver@mail.ru");
        d.setId(2L);

        Client c = new Client();
        c.setName("cClient");
        c.setPassword("passwordc");
        c.setEmail("cclient@mail.ru");
        c.setId(2L);

        when(orderRepository.findById(4L)).thenReturn(java.util.Optional.empty());
        assertFalse(orderService.updateOrder(4L, orders.get(0), c, d));
    }

    @Test
    public void updateOrderTest2() {
        Driver d = new Driver();
        d.setName("dDriver");
        d.setBusy(false);
        d.setPassword("passwordd");
        d.setEmail("driver@mail.ru");
        d.setId(2L);

        Client c = new Client();
        c.setName("cClient");
        c.setPassword("passwordc");
        c.setEmail("cclient@mail.ru");
        c.setId(2L);

        when(orderRepository.findById(2L)).thenReturn(java.util.Optional.ofNullable(orders.get(1)));
        assertTrue(orderService.updateOrder(2L, orders.get(0), c, d));
        verify(orderRepository, times(1)).changePrice(orders.get(0).getPrice(), orders.get(1).getId());
        verify(orderRepository, times(1)).changeClient(c, orders.get(1).getId());
        verify(orderRepository, times(1)).changeDriver(d, orders.get(1).getId());

    }
}
