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
import taxi.project.demo.services.CarService;
import taxi.project.demo.services.DriverService;

import java.util.Base64;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;
    private final DriverService driverService;
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public CarController(CarService carService, DriverService driverService) {
        this.carService = carService;
        this.driverService = driverService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getAllCars() {
        return new ResponseEntity<>(carService.findAllCars(), HttpStatus.OK);
    }

    @GetMapping("{carId}")
    @PreAuthorize("hasAnyRole('ROLE_DRIVER', 'ROLE_ADMIN')")
    public ResponseEntity<Object> getTheCar(@PathVariable("carId") Long carId) {
        Car carById = carService.findCarById(carId);
        if(carById == null) {
            throw new ResourceNotFoundException("Car does not exist");
        }
        return new ResponseEntity<>(carById, HttpStatus.OK);
    }

    @PatchMapping("{carId}")
    @PreAuthorize("hasAnyRole('ROLE_DRIVER', 'ROLE_ADMIN')")
    public ResponseEntity<Object> changeModel(@PathVariable("carId") Long carId, @RequestParam String model,
                                              @RequestHeader("Authorization") String authorization) throws JsonProcessingException {
        Car carById = carService.findCarById(carId);
        if(carById == null) {
            throw new ResourceNotFoundException("Car does not exist");
        }

        authorization = authorization.replace("Bearer ", "");
        String[] parts = authorization.split("\\.");

        byte[] decodedBytes = Base64.getDecoder().decode(parts[1]);
        String decodedString = new String(decodedBytes);
        JsonNode node = mapper.readTree(decodedString);
        String email = node.get("email").asText();
        String role = node.get("role").asText();

        Driver driver = (Driver) driverService.loadUserByUsername(email);
        if(role.equalsIgnoreCase("admin")) {
            carById = carService.changeModel(carById, model);
            return new ResponseEntity<>(carById, HttpStatus.OK);
        }
        if(driver != null && driver.getCar().getId().equals(carId)){
            carById = carService.changeModel(carById, model);
            return new ResponseEntity<>(carById, HttpStatus.OK);
        }
        throw new MethodNotAllowed("Sorry, this is confidential information");
    }

    @PutMapping("{carId}")
    @PreAuthorize("hasAnyRole('ROLE_DRIVER', 'ROLE_ADMIN')")
    public ResponseEntity<Object> changeModel(@PathVariable("carId") Long carId, @RequestBody Car car,
                                              @RequestHeader("Authorization") String authorization) throws JsonProcessingException {
        Car carById = carService.findCarById(carId);
        if(carById == null) {
            throw new ResourceNotFoundException("Car does not exist");
        }

        authorization = authorization.replace("Bearer ", "");
        String[] parts = authorization.split("\\.");

        byte[] decodedBytes = Base64.getDecoder().decode(parts[1]);
        String decodedString = new String(decodedBytes);
        JsonNode node = mapper.readTree(decodedString);
        String email = node.get("email").asText();
        String role = node.get("role").asText();

        Driver driver = (Driver) driverService.loadUserByUsername(email);
        if(role.equalsIgnoreCase("admin")) {
            carById = carService.updateCar(carById, car, driver);
            return new ResponseEntity<>(carById, HttpStatus.OK);
        }
        if(driver != null && driver.getCar().getId().equals(carId)){
            carById = carService.updateCar(carById, car, driver);
            return new ResponseEntity<>(carById, HttpStatus.OK);
        }
        throw new MethodNotAllowed("Sorry, this is confidential information");
    }

    @DeleteMapping("{carId}")
    @PreAuthorize("hasAnyRole('ROLE_DRIVER', 'ROLE_ADMIN')")
    public ResponseEntity<Object> deleteTheCar(@PathVariable("carId") Long carId,
                                              @RequestHeader("Authorization") String authorization) throws JsonProcessingException {
        Car carById = carService.findCarById(carId);
        if(carById == null) {
            throw new ResourceNotFoundException("Car does not exist");
        }

        authorization = authorization.replace("Bearer ", "");
        String[] parts = authorization.split("\\.");

        byte[] decodedBytes = Base64.getDecoder().decode(parts[1]);
        String decodedString = new String(decodedBytes);
        JsonNode node = mapper.readTree(decodedString);
        String email = node.get("email").asText();
        String role = node.get("role").asText();

        Driver driver = (Driver) driverService.loadUserByUsername(email);
        if(role.equalsIgnoreCase("admin")) {
            carService.deleteCar(carId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if(driver != null && driver.getCar().getId().equals(carId)){
            carService.deleteCar(carId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new MethodNotAllowed("Sorry, this is confidential information");
    }
}
