package taxi.project.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taxi.project.demo.entities.Car;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.services.DriverService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    private ObjectMapper mapper = new ObjectMapper();

    private final DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    public ResponseEntity<Object> createDriver(@RequestBody String payload) throws JsonProcessingException {
        JsonNode node = mapper.readTree(payload);
        JsonNode driver = node.get("driver");
        JsonNode car = node.get("car");

        Driver dr = mapper.readValue(driver.toPrettyString(), Driver.class);
        Car car1 = mapper.readValue(car.toPrettyString(), Car.class);

        return new ResponseEntity<>(HttpStatus.valueOf(driverService.saveDriver(dr, car1)));
    }

    @GetMapping
    public ResponseEntity<Object> getAllDrivers() throws JsonProcessingException {
        List<Driver> drivers = new ArrayList<>(driverService.getAllDrivers());
        return new ResponseEntity<>(drivers, HttpStatus.OK);
    }
}
