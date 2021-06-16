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
import taxi.project.demo.entities.Driver;
import taxi.project.demo.exceptions.MethodNotAllowed;
import taxi.project.demo.exceptions.ResourceNotFoundException;
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Client> allClients() {
        return clientService.findAllClients();
    }

    @GetMapping("{clientId}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<Object> getClientWithId(@PathVariable("clientId") Long clientId,
                                  @RequestHeader("Authorization") String authorization) throws JsonProcessingException {
        Client client = clientService.findClientById(clientId);
        if(client == null) {
            throw new ResourceNotFoundException("Client does not exist");
        }
        authorization = authorization.replace("Bearer ", "");
        String[] parts = authorization.split("\\.");

        byte[] decodedBytes = Base64.getDecoder().decode(parts[1]);
        String decodedString = new String(decodedBytes);
        JsonNode node = mapper.readTree(decodedString);
        String email = node.get("email").asText();
        String role = node.get("role").asText();

        if(role.equalsIgnoreCase("role_admin")) {
            return new ResponseEntity<>(client, HttpStatus.OK);
        }
        Client c = (Client) clientService.loadUserByUsername(email);
        if(c == null || !c.getId().equals(clientId)) {
            throw new MethodNotAllowed("Sorry, this is confidential information");
        }
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @DeleteMapping("{clientId}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    public ResponseEntity<Object> deleteClient(@PathVariable("clientId") Long clientId,
                                               @RequestHeader("Authorization") String authorization) throws JsonProcessingException {
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
            throw new ResourceNotFoundException("Client does not exist");
        }
    }

    @PutMapping("{clientId}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    public ResponseEntity<Object> updateClient(@PathVariable("clientId") Long clientId, @RequestBody Client client,
                                               @RequestHeader("Authorization") String authorization) throws JsonProcessingException {
        Client c = clientService.findClientById(clientId);
        if(c != null) {
            authorization = authorization.replace("Bearer ", "");
            String[] parts = authorization.split("\\.");

            byte[] decodedBytes = Base64.getDecoder().decode(parts[1]);
            String decodedString = new String(decodedBytes);
            JsonNode node = mapper.readTree(decodedString);
            String email = node.get("email").asText();
            String role = node.get("role").asText();

            if(role.equalsIgnoreCase("role_admin")) {
                c = clientService.updateClient(client, c);
                return new ResponseEntity<>(c, HttpStatus.OK);
            }

            Client cl = (Client) clientService.loadUserByUsername(email);
            if(cl == null || !c.getId().equals(clientId)) {
                throw new MethodNotAllowed("Sorry, this is confidential information");
            }
            c = clientService.updateClient(client, c);
            return new ResponseEntity<>(c, HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Client does not exist");
    }
}