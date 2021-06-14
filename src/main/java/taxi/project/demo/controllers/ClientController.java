package taxi.project.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taxi.project.demo.entities.Client;
import taxi.project.demo.services.ClientService;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ObjectMapper mapper = new ObjectMapper();
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public List<Client> allStudents() throws JsonProcessingException {
        return clientService.findAllClients();
    }

    @GetMapping("{clientId}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public Client getClientWithId(@PathVariable("clientId") Long clientId) {
        return clientService.findClientById(clientId);
    }

    @DeleteMapping("{clientId}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    public ResponseEntity<Object> deleteClient(@PathVariable("clientId") Long clientId, @RequestHeader("Authorization") String authorization) throws JsonProcessingException {
        authorization = authorization.replace("Bearer ", "");
        String[] parts = authorization.split("\\.");

        byte[] decodedBytes = Base64.getDecoder().decode(parts[1]);
        String decodedString = new String(decodedBytes);

        JsonNode node = mapper.readTree(decodedString);
        String email = node.get("email").asText();

        Client client = (Client) clientService.loadUserByUsername(email);

        if(clientService.deleteClient(clientId, client)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{clientId}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    public ResponseEntity<Object> updateClient(@PathVariable("clientId") Long clientId, @RequestBody Client client) {
        //TODO
        Client c = clientService.findClientById(clientId);
        if(c != null) {
            clientService.updateClient(client, c);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
