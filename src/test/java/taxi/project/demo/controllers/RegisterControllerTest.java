package taxi.project.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import taxi.project.demo.entities.Car;
import taxi.project.demo.entities.Client;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.exceptions.MethodNotAllowed;
import taxi.project.demo.services.ClientService;
import taxi.project.demo.services.DriverService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RegisterControllerTest {

    private final ClientService clientService = Mockito.mock(ClientService.class);
    private final DriverService driverService = Mockito.mock(DriverService.class);
    private final RegisterController registerController = new RegisterController(
            clientService,
            driverService
    );

    private final String clientPayload = "{\n" +
            "    \"name\" : \"Andrew\",\n" +
            "    \"password\" : \"password\",\n" +
            "    \"email\" : \"andy@email.com\"\n" +
            "}";
    private Client client = new Client();

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

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        client = new ObjectMapper().readValue(clientPayload, Client.class);
        driver = new ObjectMapper().readValue(driverPayload, Driver.class);
        car = new ObjectMapper().readValue(carPayload, Car.class);
    }

    @Test
    public void registerClientTest1() throws JsonProcessingException {
        when(clientService.registerClient(client)).thenReturn(405);
        assertThrows(MethodNotAllowed.class, () ->
            {
                registerController.registerClient(clientPayload, "client");
            });
    }

    @Test
    public void registerClientTest2() throws JsonProcessingException {
        when(clientService.registerClient(client)).thenReturn(201);
        assertEquals(new ResponseEntity<>(HttpStatus.CREATED), registerController.registerClient(clientPayload, "client"));
    }

    @Test
    public void registerClientTest3() throws JsonProcessingException {
        when(clientService.registerClient(client)).thenReturn(0);
        assertEquals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), registerController.registerClient(clientPayload, "client"));
    }

    @Test
    public void registerClientTest4() {
        when(driverService.saveDriver(any(Driver.class), any(Car.class))).thenReturn(405);
        assertThrows(MethodNotAllowed.class, () ->
        {
            registerController.registerClient(driverCarPayload, "driver");
        });
    }
}
