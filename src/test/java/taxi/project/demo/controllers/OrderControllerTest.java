package taxi.project.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import taxi.project.demo.entities.Client;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.entities.Order;
import taxi.project.demo.exceptions.ResourceNotFoundException;
import taxi.project.demo.services.ClientService;
import taxi.project.demo.services.DriverService;
import taxi.project.demo.services.OrderService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private final ClientService clientService = Mockito.mock(ClientService.class);
    private final DriverService driverService = Mockito.mock(DriverService.class);
    private final OrderService orderService = Mockito.mock(OrderService.class);
    private final OrderController orderController = new OrderController(
            clientService,
            driverService,
            orderService
    );

    private List<Driver> drivers = new ArrayList<>();
    private List<Client> clients = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private final String adminAuth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQGVtYWlsLmNvbSIsInJvbGUiOiJST0xFX0FETUlOIn0.edJrpMq7I6a5E8aIIRtYij6uNB2PNJYJ4GASuwDtTwQxMKG6FNpHuijHPi02dwYVwa9JEaRsi2_SFLlN_s3Ggw";
    private final String clientAuth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoiQW5kcmV3IiwiZW1haWwiOiJhbmR5QGVtYWlsLmNvbSIsInJvbGUiOiJST0xFX0NMSUVOVCJ9.JpXjb17Gev4uOjyN3MHk7g7TZg9i2d-YzrEgij8qpihpan-9EVXEbVrU0BPF0-3TITCjdfCJNnmGCRpcn29hUg";
    private final String driverAuth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoiTXIuUm9jayIsImVtYWlsIjoicm9ja0BlbWFpbC5jb20iLCJyb2xlIjoiUk9MRV9EUklWRVIiLCJpc0J1c3kiOmZhbHNlLCJjYXJNb2RlbCI6IkJNVyJ9.tgkXCvppwrAGU04tGAhcnn4WhYRInGec1OxJY2x7W0-PSLa9CinmGgLXh3WZgq56DneVWI0S1WlgWcabRzlTNw";
    private final String drEmail = "driver@email.com";
    private final String clEmail = "andy@email.com";

    @BeforeEach
    public void setUp() {
        clients.add(new Client(1L, "FClient", "password", clEmail, "ROLE_CLIENT", null));
        clients.add(new Client(2L, "FClient", "password", clEmail, "ROLE_CLIENT", null));
        clients.add(new Client(3L, "FClient", "password", clEmail, "ROLE_CLIENT", null));

        drivers.add(new Driver(1L, "driver", drEmail, "password", "ROLE_DRIVER",true,  null, null));
        drivers.add(new Driver(2L, "driver", drEmail, "password", "ROLE_DRIVER",true,  null, null));
        drivers.add(new Driver(3L, "driver", drEmail, "password", "ROLE_DRIVER",true,  null, null));

        orders.add(new Order(1L, clients.get(0), drivers.get(0), 80));
        orders.add(new Order(2L, clients.get(1), drivers.get(1), 80));
        clients.get(0).setOrders(List.of(orders.get(0)));
    }

    @Test
    public void createOrderTest1() {
        when(clientService.findClientById(4L)).thenReturn(null);
        when(driverService.findDriverById(1L)).thenReturn(drivers.get(0));
        assertThrows(ResourceNotFoundException.class, () -> {
           orderController.createOrder(4L, 1L);
        });
    }

    @Test
    public void getOrderOfClientTest1() {
        when(orderService.getAllOrdersOfClient(1L)).thenReturn(orders);
        assertEquals(new ResponseEntity<>(orders, HttpStatus.OK), orderController.getOrdersOfClient(1L));
    }

    @Test
    public void getAllOrdersTest1() {
        when(orderService.getAllOrders()).thenReturn(orders);
        assertEquals(new ResponseEntity<>(orders, HttpStatus.OK), orderController.getAllOrders());
    }

    @Test
    public void getSpecificOrderTest1() {
        when(orderService.findOrderById(2L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, ()-> {
           orderController.getSpecificOrder(2L, clientAuth);
        });
    }

    @Test
    public void getSpecificOrderTest2() throws JsonProcessingException {
        when(orderService.findOrderById(1L)).thenReturn(orders.get(0));
        assertEquals(new ResponseEntity<>(orders.get(0), HttpStatus.OK),
                orderController.getSpecificOrder(1L, adminAuth));
    }

    @Test
    public void deleteSpecificOrderTest1() throws JsonProcessingException {
        when(orderService.findOrderById(3L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, ()-> {
            orderController.deleteSpecificOrder(3L, clientAuth);
        });
    }

    @Test
    public void deleteSpecificOrderTest2() throws JsonProcessingException {
        when(orderService.findOrderById(1L)).thenReturn(orders.get(0));
        assertEquals(new ResponseEntity<>( HttpStatus.OK),
                orderController.deleteSpecificOrder(1L, adminAuth));
    }

}
