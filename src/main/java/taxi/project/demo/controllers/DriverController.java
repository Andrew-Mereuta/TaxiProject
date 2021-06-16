package taxi.project.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taxi.project.demo.entities.Car;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.exceptions.MethodNotAllowed;
import taxi.project.demo.exceptions.ResourceNotFoundException;
import taxi.project.demo.services.DriverService;

import java.util.ArrayList;
import java.util.Base64;
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


    // Admin system to create a driver, but driver can be created from register controller as normal user
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> createDriver(@RequestBody String payload) throws JsonProcessingException {
        JsonNode node = mapper.readTree(payload);
        JsonNode driver = node.get("driver");
        JsonNode car = node.get("car");

        Driver dr = mapper.readValue(driver.toPrettyString(), Driver.class);
        Car car1 = mapper.readValue(car.toPrettyString(), Car.class);

        return new ResponseEntity<>(HttpStatus.valueOf(driverService.saveDriver(dr, car1)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getAllDrivers() throws JsonProcessingException {
        List<Driver> drivers = new ArrayList<>(driverService.getAllDrivers());
        return new ResponseEntity<>(drivers, HttpStatus.OK);
    }

    @GetMapping("{driverId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DRIVER')")
    public ResponseEntity<Object> getDriverById(@PathVariable("driverId") Long driverId,
                                                @RequestHeader("Authorization") String authorization) throws JsonProcessingException {
        Driver driver = driverService.findDriverById(driverId);
        if(driver == null) {
            throw new ResourceNotFoundException("Driver does not exist");
        }

        authorization = authorization.replace("Bearer ", "");
        String[] parts = authorization.split("\\.");

        byte[] decodedBytes = Base64.getDecoder().decode(parts[1]);
        String decodedString = new String(decodedBytes);
        JsonNode node = mapper.readTree(decodedString);
        String email = node.get("email").asText();
        String role = node.get("role").asText();

        if(role.equalsIgnoreCase("ROLE_ADMIN")) {
            return new ResponseEntity<>(driver, HttpStatus.OK);
        }

        Driver dr = (Driver) driverService.loadUserByUsername(email);
        if(dr == null || !dr.getId().equals(driverId)) {
            throw new MethodNotAllowed("Sorry, this is confidential information");
        }
        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

    @DeleteMapping("{driverId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DRIVER')")
    public ResponseEntity<Object> deleteDriverById(@PathVariable("driverId") Long driverId,
                                                   @RequestHeader("Authorization") String authorization) throws JsonProcessingException {
        Driver driver = driverService.findDriverById(driverId);
        if(driver == null) {
            throw new ResourceNotFoundException("Driver does not exist");
        }

        authorization = authorization.replace("Bearer ", "");
        String[] parts = authorization.split("\\.");

        byte[] decodedBytes = Base64.getDecoder().decode(parts[1]);
        String decodedString = new String(decodedBytes);
        JsonNode node = mapper.readTree(decodedString);
        String email = node.get("email").asText();
        String role = node.get("role").asText();

        if(role.equalsIgnoreCase("ROLE_ADMIN")) {
            driverService.deleteDriverById(driverId);
            return new ResponseEntity<>(driver, HttpStatus.OK);
        }

        Driver dr = (Driver) driverService.loadUserByUsername(email);
        if(dr == null || !dr.getId().equals(driverId)) {
            throw new MethodNotAllowed("Sorry, this is confidential information");
        }
        driverService.deleteDriverById(driverId);
        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

    @PutMapping("{driverId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DRIVER')")
    public ResponseEntity<Object> updateDriver(@PathVariable("driverId") Long driverId,
                                                   @RequestHeader("Authorization") String authorization,
                                                   @RequestBody Driver driver) throws JsonProcessingException {
        Driver currentDriver = driverService.findDriverById(driverId);
        if(currentDriver == null) {
            throw new ResourceNotFoundException("Driver does not exist");
        }

        authorization = authorization.replace("Bearer ", "");
        String[] parts = authorization.split("\\.");

        byte[] decodedBytes = Base64.getDecoder().decode(parts[1]);
        String decodedString = new String(decodedBytes);
        JsonNode node = mapper.readTree(decodedString);
        String email = node.get("email").asText();
        String role = node.get("role").asText();

        if(role.equalsIgnoreCase("ROLE_ADMIN")) {
            currentDriver = driverService.updateDriver(driver, currentDriver);
            return new ResponseEntity<>(currentDriver, HttpStatus.OK);
        }

        Driver dr = (Driver) driverService.loadUserByUsername(email);
        if(dr == null || !dr.getId().equals(driverId)) {
            throw new MethodNotAllowed("Sorry, this is confidential information");
        }
        currentDriver = driverService.updateDriver(driver, currentDriver);
        return new ResponseEntity<>(currentDriver, HttpStatus.OK);
    }


}
