package taxi.project.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taxi.project.demo.services.ClientService;
import taxi.project.demo.services.DriverService;
import taxi.project.demo.services.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private ClientService clientService;
    private DriverService driverService;
    private OrderService orderService;

    @Autowired
    public OrderController(ClientService clientService,
                           DriverService driverService,
                           OrderService orderService) {
        this.clientService = clientService;
        this.driverService = driverService;
        this. orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<Object> createOrder(@RequestParam(value = "clientId") Long clientId,
                                              @RequestParam(value = "driverId") Long driverId) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
