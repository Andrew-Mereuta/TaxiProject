package taxi.project.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import taxi.project.demo.entities.Car;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.exceptions.MethodNotAllowed;
import taxi.project.demo.exceptions.ResourceNotFoundException;
import taxi.project.demo.services.DriverService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DriverControllerTest {

    private final DriverService driverService = Mockito.mock(DriverService.class);
    private final DriverController driverController = new DriverController(
            driverService
    );

    private final String driverCarPayload = "{\n" +
            "  \"driver\": {\n" +
            "    \"name\": \"Mr.Rock\",\n" +
            "    \"email\": \"rock@email.com\",\n" +
            "    \"password\": \"password\"\n" +
            "  },\n" +
            "  \"car\": {\n" +
            "    \"model\": \"BMW\"\n" +
            "  }\n" +
            "}";
    private final String driverPayload = "{\n" +
            "    \"name\": \"Mr.Rock\",\n" +
            "    \"email\": \"rock@email.com\",\n" +
            "    \"password\": \"password\"\n" +
            "  }";
    private final String carPayload = "{\n" +
            "    \"model\": \"BMW\"\n" +
            "  }";

    private Driver driver = new Driver();
    private Car car = new Car();
    private List<Driver> drivers = new ArrayList<>();
    private final String drEmail = "rock@email.com";
    private final String driverAuth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoiTXIuUm9jayIsImVtYWlsIjoicm9ja0BlbWFpbC5jb20iLCJyb2xlIjoiUk9MRV9EUklWRVIiLCJpc0J1c3kiOmZhbHNlLCJjYXJNb2RlbCI6IkJNVyJ9.tgkXCvppwrAGU04tGAhcnn4WhYRInGec1OxJY2x7W0-PSLa9CinmGgLXh3WZgq56DneVWI0S1WlgWcabRzlTNw";
    private final String adminAuth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQGVtYWlsLmNvbSIsInJvbGUiOiJST0xFX0FETUlOIn0.edJrpMq7I6a5E8aIIRtYij6uNB2PNJYJ4GASuwDtTwQxMKG6FNpHuijHPi02dwYVwa9JEaRsi2_SFLlN_s3Ggw";

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        driver = new ObjectMapper().readValue(driverPayload, Driver.class);
        car = new ObjectMapper().readValue(carPayload, Car.class);
        drivers.add(new Driver(1L, "driver", drEmail, "password", "ROLE_DRIVER",true,  null, null));
        drivers.add(new Driver(2L, "driver", drEmail, "password", "ROLE_DRIVER",true,  null, null));
        drivers.add(new Driver(3L, "driver", drEmail, "password", "ROLE_DRIVER",true,  null, null));
    }

    @Test
    public void createDriverTest() throws JsonProcessingException {
        when(driverService.saveDriver(any(Driver.class), any(Car.class))).thenReturn(201);
        assertEquals(new ResponseEntity<>(HttpStatus.CREATED), driverController.createDriver(driverCarPayload));
    }

    @Test
    public void getAllDriversTest() throws JsonProcessingException {
        when(driverService.getAllDrivers()).thenReturn(List.of(driver));
        assertEquals(new ResponseEntity<>(List.of(driver), HttpStatus.OK), driverController.getAllDrivers());
    }

    @Test
    public void getDriverByIdTest1() {
        when(driverService.findDriverById(4L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> {
           driverController.getDriverById(4L, driverAuth);
        });
    }

    @Test
    public void getDriverByIdTest2() throws JsonProcessingException {
        when(driverService.findDriverById(1L)).thenReturn(drivers.get(0));
        assertEquals(new ResponseEntity<>(drivers.get(0), HttpStatus.OK), driverController.getDriverById(1L, adminAuth));
    }

    @Test
    public void getDriverByIdTest3() throws JsonProcessingException {
        when(driverService.findDriverById(1L)).thenReturn(drivers.get(0));
        when(driverService.loadUserByUsername(drEmail)).thenReturn(drivers.get(0));
        assertEquals(new ResponseEntity<>(drivers.get(0), HttpStatus.OK), driverController.getDriverById(1L, driverAuth));
    }

    @Test
    public void getDriverByIdTest4() throws JsonProcessingException {
        when(driverService.findDriverById(1L)).thenReturn(drivers.get(0));
        when(driverService.loadUserByUsername(drEmail)).thenReturn(null);
        assertThrows(MethodNotAllowed.class, () -> {
            driverController.getDriverById(1L, driverAuth);
        });
    }

    @Test
    public void deleteDriverByIdTest1() {
        when(driverService.findDriverById(4L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> {
            driverController.deleteDriverById(4L, driverAuth);
        });
    }

    @Test
    public void deleteDriverByIdTest2() throws JsonProcessingException {
        when(driverService.findDriverById(1L)).thenReturn(drivers.get(0));
        assertEquals(new ResponseEntity<>(drivers.get(0), HttpStatus.OK),
                driverController.deleteDriverById(1L, adminAuth));
    }

    @Test
    public void deleteDriverByIdTest3() throws JsonProcessingException {
        when(driverService.findDriverById(1L)).thenReturn(drivers.get(0));
        when(driverService.loadUserByUsername(drEmail)).thenReturn(drivers.get(0));
        assertEquals(new ResponseEntity<>(drivers.get(0), HttpStatus.OK), driverController.deleteDriverById(1L, driverAuth));
    }

    @Test
    public void deleteDriverByIdTest4() throws JsonProcessingException {
        when(driverService.findDriverById(1L)).thenReturn(drivers.get(0));
        when(driverService.loadUserByUsername(drEmail)).thenReturn(null);
        assertThrows(MethodNotAllowed.class, () -> {
            driverController.deleteDriverById(1L, driverAuth);
        });
    }

    @Test
    public void updateDriverTest1() {
        when(driverService.findDriverById(4L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> {
            driverController.updateDriver(4L, driverAuth, new Driver());
        });
    }

    @Test
    public void updateDriverTest2() throws JsonProcessingException {
        when(driverService.findDriverById(1L)).thenReturn(drivers.get(0));
        when(driverService.updateDriver(any(Driver.class), any(Driver.class))).thenReturn(drivers.get(1));
        assertEquals(new ResponseEntity<>(drivers.get(1), HttpStatus.OK),
                driverController.updateDriver(1L, adminAuth, drivers.get(1)));
    }

    @Test
    public void updateDriverTest3() throws JsonProcessingException {
        when(driverService.findDriverById(1L)).thenReturn(drivers.get(0));
        when(driverService.updateDriver(any(Driver.class), any(Driver.class))).thenReturn(drivers.get(1));
        when(driverService.loadUserByUsername(drEmail)).thenReturn(drivers.get(0));
        assertEquals(new ResponseEntity<>(drivers.get(1), HttpStatus.OK),
                driverController.updateDriver(1L, driverAuth, drivers.get(1)));
    }

    @Test
    public void updateDriverTest4() throws JsonProcessingException {
        when(driverService.findDriverById(1L)).thenReturn(drivers.get(0));
        when(driverService.loadUserByUsername(drEmail)).thenReturn(null);

        assertThrows(MethodNotAllowed.class, () -> {
            driverController.updateDriver(1L, driverAuth, drivers.get(1));
        });
    }
}
