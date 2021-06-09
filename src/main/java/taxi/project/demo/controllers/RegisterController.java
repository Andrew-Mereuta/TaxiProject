package taxi.project.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import taxi.project.demo.entities.Client;
import taxi.project.demo.services.ClientService;

@RestController
@RequestMapping("/api/v1/register")
public class RegisterController {

    private ClientService clientService;

    @Autowired
    public RegisterController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity registerClient(@RequestBody Client client) {
        int result = clientService.registerClient(client);
        if(result == 405) {
            return new ResponseEntity(HttpStatus.METHOD_NOT_ALLOWED);
        }
        if(result == 201) {
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @PostMapping("/driver")
//    public ResponseEntity registerDriver(@RequestBody Driver driver) {
//        int result = clientService.registerDriver(driver);
//        if(result == 405) {
//            return new ResponseEntity(HttpStatus.METHOD_NOT_ALLOWED);
//        }
//        if(result == 201) {
//            return new ResponseEntity(HttpStatus.CREATED);
//        }
//        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
