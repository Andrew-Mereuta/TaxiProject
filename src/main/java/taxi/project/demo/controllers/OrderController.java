package taxi.project.demo.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import taxi.project.demo.entities.Client;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.entities.Order;
import taxi.project.demo.exceptions.MethodNotAllowed;
import taxi.project.demo.exceptions.ResourceNotFoundException;
import taxi.project.demo.services.ClientService;
import taxi.project.demo.services.DriverService;
import taxi.project.demo.services.OrderService;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private ClientService clientService;
    private DriverService driverService;
    private OrderService orderService;
    private ObjectMapper mapper = new ObjectMapper();
    private Random rand = new Random();

    @Autowired
    public OrderController(ClientService clientService,
                           DriverService driverService,
                           OrderService orderService) {
        this.clientService = clientService;
        this.driverService = driverService;
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    public ResponseEntity<Object> createOrder(@RequestParam(value = "clientId") Long clientId,
                                              @RequestParam(value = "driverId") Long driverId) {
        Client clientById = clientService.findClientById(clientId);
        Driver driverById = driverService.findDriverById(driverId);
        if(clientById == null || driverById == null) {
            throw new ResourceNotFoundException("Either Driver or Client does not exist");
        }
        Order order = new Order();
        order.setClient(clientById);
        order.setDriver(driverById);
        order.setPrice(Math.floor(Math.random() * (100 - 20 + 1) + 20));
        int result = orderService.saveOrder(order);
        return new ResponseEntity<>(order, HttpStatus.valueOf(result));
    }


    @GetMapping("/clients/{clientId}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    public ResponseEntity<Object> getOrdersOfClient(@PathVariable("clientId") Long clientId) {
        List<Order> orders = new ArrayList<>(orderService.getAllOrdersOfClient(clientId));
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/drivers/{driverId}")
    @PreAuthorize("hasAnyRole('ROLE_DRIVER', 'ROLE_ADMIN')")
    public ResponseEntity<Object> getOrdersOfDiver(@PathVariable("driverId") Long driverId) {
        List<Order> orders = new ArrayList<>(orderService.getAllOrdersOfDriver(driverId));
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_DRIVER')")
    public ResponseEntity<Object> getSpecificOrder(@PathVariable("orderId") Long orderId,
                                                   @RequestHeader("Authorization") String authorization) throws JsonProcessingException {
        Order order = orderService.findOrderById(orderId);
        if(order == null) {
            throw new ResourceNotFoundException("Order does not exist");
        }

        authorization = authorization.replace("Bearer ", "");
        String[] parts = authorization.split("\\.");

        byte[] decodedBytes = Base64.getDecoder().decode(parts[1]);
        String decodedString = new String(decodedBytes);

        JsonNode node = mapper.readTree(decodedString);
        String email = node.get("email").asText();
        String role = node.get("role").asText();

        if(role.equalsIgnoreCase("ROLE_ADMIN") || order.getClient().getEmail().equals(email) || order.getDriver().getEmail().equals(email)) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        throw new MethodNotAllowed("Sorry, this is confidential information");
    }

    @DeleteMapping("{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_DRIVER')")
    public ResponseEntity<Object> deleteSpecificOrder(@PathVariable("orderId") Long orderId,
                                                      @RequestHeader("Authorization") String authorization) throws JsonProcessingException {
        Order order = orderService.findOrderById(orderId);
        if(order == null) {
            throw new ResourceNotFoundException("Order does not exist");
        }

        authorization = authorization.replace("Bearer ", "");
        String[] parts = authorization.split("\\.");

        byte[] decodedBytes = Base64.getDecoder().decode(parts[1]);
        String decodedString = new String(decodedBytes);

        JsonNode node = mapper.readTree(decodedString);
        String email = node.get("email").asText();
        String role = node.get("role").asText();

        if(role.equalsIgnoreCase("ROLE_ADMIN") || order.getClient().getEmail().equals(email) || order.getDriver().getEmail().equals(email)) {
            orderService.deleteOrder(orderId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new MethodNotAllowed("Sorry, this is confidential information");
    }

    @PatchMapping("{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    public ResponseEntity<Object> changeDriver(@PathVariable("orderId") Long orderId,
                                               @RequestParam(value = "newDriverId") Long driverId,
                                               @RequestHeader("Authorization") String authorization) throws JsonProcessingException {
        Order order = orderService.findOrderById(orderId);
        Driver driver = driverService.findDriverById(driverId);
        if(order == null || driver == null) {
            throw new ResourceNotFoundException("Order or Driver does not exist");
        }

        authorization = authorization.replace("Bearer ", "");
        String[] parts = authorization.split("\\.");

        byte[] decodedBytes = Base64.getDecoder().decode(parts[1]);
        String decodedString = new String(decodedBytes);

        JsonNode node = mapper.readTree(decodedString);
        String email = node.get("email").asText();
        String role = node.get("role").asText();

        if(role.equalsIgnoreCase("ROLE_ADMIN") || order.getClient().getEmail().equals(email)) {
            orderService.changeDriver(order, driver);
            order = orderService.findOrderById(orderId);
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        throw new MethodNotAllowed("Sorry, this is confidential information");
    }

    @PutMapping("{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')") // In the production, only admin will be able to to do it
    public ResponseEntity<Object> updateOrder(@PathVariable("orderId") Long orderId, @RequestBody Order order) {
        Client client = order.getClient();
        Driver driver = order.getDriver();
        client = (Client) clientService.loadUserByUsername(client.getEmail());
        driver = (Driver) driverService.loadUserByUsername(driver.getEmail());

        if(driver == null || client ==null) {
            throw new ResourceNotFoundException("Client or driver does not exist");
        }

        if(orderService.updateOrder(orderId, order, client, driver)) {
            order = orderService.findOrderById(orderId);
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        throw new MethodNotAllowed("Sorry, this is confidential information");
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
