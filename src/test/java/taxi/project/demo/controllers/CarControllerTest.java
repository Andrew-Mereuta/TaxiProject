package taxi.project.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import taxi.project.demo.entities.Car;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.exceptions.MethodNotAllowed;
import taxi.project.demo.exceptions.ResourceNotFoundException;
import taxi.project.demo.services.CarService;
import taxi.project.demo.services.DriverService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CarControllerTest {

    private final CarService carService = Mockito.mock(CarService.class);
    private final DriverService driverService = Mockito.mock(DriverService.class);
    private CarController carController = new CarController(
            carService,
            driverService
    );

    private List<Car> cars = new ArrayList<>();
    private Driver originalDriver = new Driver();

    private final String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoiTXIuUm9jayIsImVtYWlsIjoicm9ja0BlbWFpbC5jb20iLCJyb2xlIjoiUk9MRV9EUklWRVIiLCJpc0J1c3kiOmZhbHNlLCJjYXJNb2RlbCI6IkJNVyJ9.tgkXCvppwrAGU04tGAhcnn4WhYRInGec1OxJY2x7W0-PSLa9CinmGgLXh3WZgq56DneVWI0S1WlgWcabRzlTNw";
    private final String forAdmin = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoiTXIuUm9jayIsImVtYWlsIjoicm9ja0BlbWFpbC5jb20iLCJyb2xlIjoiUk9MRV9BRE1JTiIsImlzQnVzeSI6ZmFsc2UsImNhck1vZGVsIjoiQk1XIn0=.tgkXCvppwrAGU04tGAhcnn4WhYRInGec1OxJY2x7W0-PSLa9CinmGgLXh3WZgq56DneVWI0S1WlgWcabRzlTNw";

    @BeforeEach
    void setUp() {
        originalDriver.setName("driver");
        originalDriver.setPassword("password");
        originalDriver.setRole("ROLE_DRIVER");
        originalDriver.setEmail("rock@email.com");
        originalDriver.setId(1L);

        cars.add(new Car(1L, "Ferrari", originalDriver));
        cars.add(new Car(2L, "Mers", originalDriver));
        cars.add(new Car(3L, "BMW", originalDriver));
        originalDriver.setCar(cars.get(0));
    }

    @Test
    public void getAllCarsTest() {
        when(carService.findAllCars()).thenReturn(cars);
        assertEquals(new ResponseEntity<>(cars, HttpStatus.OK), carController.getAllCars());
    }

    @Test
    public void getTheCarTest1() {
        when(carService.findCarById(1L)).thenReturn(cars.get(0));
        assertEquals(new ResponseEntity<>(cars.get(0), HttpStatus.OK), carController.getTheCar(1L));
    }

    @Test
    public void getTheCarTest2() {
        when(carService.findCarById(4L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> {
           carController.getTheCar(4L);
        });
    }

    @Test
    public void changeModelTest1() {
        when(carService.findCarById(4L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> {
            carController.changeModel(4L, "NEWMODEL", auth);
        });
    }

    @Test
    public void changeModelTest2() throws JsonProcessingException {
        String model = "New Model";
        when(carService.findCarById(1L)).thenReturn(cars.get(0));
        when(driverService.loadUserByUsername("rock@email.com")).thenReturn(originalDriver);
        Car result = cars.get(0);
        result.setModel(model);
        when(carService.changeModel(cars.get(0), model)).thenReturn(result);
        assertEquals(new ResponseEntity<>(result, HttpStatus.OK), carController.changeModel(1L,model, auth));
    }

    @Test
    public void changeModelTest3() throws JsonProcessingException {
        String model = "New Model";
        when(carService.findCarById(1L)).thenReturn(cars.get(0));
        when(driverService.loadUserByUsername("rock@email.com")).thenReturn(originalDriver);
        Car result = cars.get(0);
        result.setModel(model);
        when(carService.changeModel(cars.get(0), model)).thenReturn(result);
        assertEquals(new ResponseEntity<>(result, HttpStatus.OK), carController.changeModel(1L,model, forAdmin));
    }

    @Test
    public void changeModelTest4() throws JsonProcessingException {
        String model = "New Model";
        when(carService.findCarById(2L)).thenReturn(cars.get(1));
        when(driverService.loadUserByUsername("rock@email.com")).thenReturn(originalDriver);
        assertThrows(MethodNotAllowed.class, ()->{
            carController.changeModel(2L, model, auth);
        });
    }

    @Test
    public void updateCarTest1() {
        when(carService.findCarById(4L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> {
            carController.updateCar(4L, cars.get(2), auth);
        });
    }

    @Test
    public void updateCarTest2() throws JsonProcessingException {
        String model = "New Model";
        when(carService.findCarById(1L)).thenReturn(cars.get(0));
        when(driverService.loadUserByUsername("rock@email.com")).thenReturn(originalDriver);
        Car result = cars.get(0);
        result.setModel(model);
        when(carService.updateCar(cars.get(0), cars.get(1), originalDriver)).thenReturn(result);
        assertEquals(new ResponseEntity<>(result, HttpStatus.OK), carController.updateCar(1L, cars.get(1), auth));
    }

    @Test
    public void updateCarTest3() throws JsonProcessingException {
        String model = "New Model";
        when(carService.findCarById(1L)).thenReturn(cars.get(0));
        when(driverService.loadUserByUsername("rock@email.com")).thenReturn(originalDriver);
        Car result = cars.get(0);
        result.setModel(model);
        when(carService.updateCar(cars.get(0), cars.get(1), originalDriver)).thenReturn(result);
        assertEquals(new ResponseEntity<>(result, HttpStatus.OK), carController.updateCar(1L, cars.get(1), forAdmin));
    }

    @Test
    public void updateCarTest4() throws JsonProcessingException {
        String model = "New Model";
        when(carService.findCarById(2L)).thenReturn(cars.get(1));
        when(driverService.loadUserByUsername("rock@email.com")).thenReturn(originalDriver);
        assertThrows(MethodNotAllowed.class, ()->{
            carController.updateCar(2L, cars.get(1), auth);
        });
    }

    @Test
    public void deleteTheTest1() {
        when(carService.findCarById(4L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> {
            carController.deleteTheCar(4L, auth);
        });
    }

    @Test
    public void deleteTheTest2() throws JsonProcessingException {
        when(carService.findCarById(1L)).thenReturn(cars.get(0));
        when(driverService.loadUserByUsername("rock@email.com")).thenReturn(originalDriver);
        assertEquals(new ResponseEntity<>(HttpStatus.OK), carController.deleteTheCar(1L, auth));
        verify(carService, times(1)).deleteCar(anyLong());
    }

    @Test
    public void deleteTheTest3() throws JsonProcessingException {
        when(carService.findCarById(1L)).thenReturn(cars.get(0));
        when(driverService.loadUserByUsername("rock@email.com")).thenReturn(originalDriver);
        assertEquals(new ResponseEntity<>(HttpStatus.OK), carController.deleteTheCar(1L, forAdmin));
        verify(carService, times(1)).deleteCar(anyLong());
    }

    @Test
    public void deleteTheTest4() throws JsonProcessingException {
        String model = "New Model";
        when(carService.findCarById(2L)).thenReturn(cars.get(1));
        when(driverService.loadUserByUsername("rock@email.com")).thenReturn(originalDriver);
        assertThrows(MethodNotAllowed.class, ()->{
            carController.deleteTheCar(2L, auth);
        });
    }

}
