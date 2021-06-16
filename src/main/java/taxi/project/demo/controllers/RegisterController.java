package taxi.project.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taxi.project.demo.entities.Car;
import taxi.project.demo.entities.Client;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.services.ClientService;
import taxi.project.demo.services.DriverService;

@RestController
@RequestMapping("/register")
public class RegisterController {

    private ClientService clientService;
    private DriverService driverService;
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public RegisterController(ClientService clientService, DriverService driverService) {
        this.clientService = clientService;
        this.driverService = driverService;
    }

    @PostMapping
    public ResponseEntity<Object> registerClient(@RequestBody String payload,
                                                 @RequestParam(value = "who", defaultValue = "client") String aim) throws JsonProcessingException {
        JsonNode node = mapper.readTree(payload);
        int result;
        if(aim.equalsIgnoreCase("driver")) {
            JsonNode driver = node.get("driver");
            JsonNode car = node.get("car");
            Driver dr = mapper.readValue(driver.toPrettyString(), Driver.class);
            Car car1 = mapper.readValue(car.toPrettyString(), Car.class);
            result = driverService.saveDriver(dr, car1);
        } else {
            Client client = mapper.readValue(payload, Client.class);
            result = clientService.registerClient(client);
        }

        if (result == 405) {
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        } else if (result == 201) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
