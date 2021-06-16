package taxi.project.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import taxi.project.demo.entities.Client;
import taxi.project.demo.exceptions.MethodNotAllowed;
import taxi.project.demo.exceptions.ResourceNotFoundException;
import taxi.project.demo.services.ClientService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ClientControllerTest {

    private final ClientService clientService = Mockito.mock(ClientService.class);
    private final ClientController clientController = new ClientController(
            clientService
    );

    private List<Client> clients = new ArrayList<>();
    private Client client = new Client();
    private final String adminAuth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQGVtYWlsLmNvbSIsInJvbGUiOiJST0xFX0FETUlOIn0.edJrpMq7I6a5E8aIIRtYij6uNB2PNJYJ4GASuwDtTwQxMKG6FNpHuijHPi02dwYVwa9JEaRsi2_SFLlN_s3Ggw";
    private final String clientAuth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoiQW5kcmV3IiwiZW1haWwiOiJhbmR5QGVtYWlsLmNvbSIsInJvbGUiOiJST0xFX0NMSUVOVCJ9.JpXjb17Gev4uOjyN3MHk7g7TZg9i2d-YzrEgij8qpihpan-9EVXEbVrU0BPF0-3TITCjdfCJNnmGCRpcn29hUg";


    @BeforeEach
    void setUp() {
        client.setName("client");
        client.setPassword("pass");
        client.setRole("ROLE_CLIENT");
        client.setEmail("andy@email.com");
        client.setId(1L);

        clients.add(new Client(1L, "FClient", "password", "andy@email.com", "ROLE_CLIENT", null));
        clients.add(new Client(2L, "FClient", "password", "andy@email.com", "ROLE_CLIENT", null));
        clients.add(new Client(3L, "FClient", "password", "andy@email.com", "ROLE_CLIENT", null));
    }

    @Test
    public void allClientsTest() {
        when(clientService.findAllClients()).thenReturn(clients);
        assertArrayEquals(clients.toArray(), clientController.allClients().toArray());
    }

    @Test
    public void getClientWithIdTest1() {
        when(clientService.findClientById(4L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> {
           clientController.getClientWithId(4L, clientAuth);
        });
    }

    @Test
    public void getClientWithIdTest2() throws JsonProcessingException {
        when(clientService.findClientById(1L)).thenReturn(clients.get(0));
        when(clientService.loadUserByUsername("andy@email.com")).thenReturn(clients.get(0));
        assertEquals(new ResponseEntity<>(clients.get(0), HttpStatus.OK), clientController.getClientWithId(1L, clientAuth));
    }

    @Test
    public void getClientWithIdTest3() throws JsonProcessingException {
        when(clientService.findClientById(1L)).thenReturn(clients.get(0));
        when(clientService.loadUserByUsername("andy@email.com")).thenReturn(clients.get(0));
        assertEquals(new ResponseEntity<>(clients.get(0), HttpStatus.OK), clientController.getClientWithId(1L, adminAuth));
    }

    @Test
    public void getClientWithIdTest4() {
        when(clientService.findClientById(1L)).thenReturn(clients.get(0));
        when(clientService.loadUserByUsername("andy@email.com")).thenReturn(null);
        assertThrows(MethodNotAllowed.class, () -> {
           clientController.getClientWithId(1L, clientAuth);
        });
    }

    @Test
    public void deleteClientTest1() throws JsonProcessingException {
        when(clientService.loadUserByUsername("andy@email.com")).thenReturn(clients.get(0));
        when(clientService.deleteClient(1L, clients.get(0))).thenReturn(true);
        assertEquals(new ResponseEntity<>(HttpStatus.OK), clientController.deleteClient(1L, clientAuth));
    }

    @Test
    public void deleteClientTest2() throws JsonProcessingException {
        when(clientService.loadUserByUsername("andy@email.com")).thenReturn(clients.get(0));
        when(clientService.deleteClient(1L, clients.get(0))).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> {
            clientController.deleteClient(1L, clientAuth);
        });
    }

    @Test
    public void updateClientTest1() {
        when(clientService.findClientById(4L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> {
            clientController.updateClient(4L, client, clientAuth);
        });
    }

    @Test
    public void updateClientTest2() throws JsonProcessingException {
        when(clientService.findClientById(1L)).thenReturn(clients.get(0));
        when(clientService.updateClient(client, clients.get(0))).thenReturn(client);
        assertEquals(new ResponseEntity<>(client, HttpStatus.OK), clientController.updateClient(1L, client, adminAuth));
    }

    @Test
    public void updateClientTest3() {
        when(clientService.findClientById(1L)).thenReturn(clients.get(0));
        when(clientService.loadUserByUsername("andy@email.com")).thenReturn(null);
        assertThrows(MethodNotAllowed.class, () -> {
            clientController.updateClient(1L, client, clientAuth);
        });
    }

    @Test
    public void updateClientTest4() throws JsonProcessingException {
        when(clientService.findClientById(1L)).thenReturn(clients.get(0));
        when(clientService.loadUserByUsername("andy@email.com")).thenReturn(clients.get(0));
        when(clientService.updateClient(client, clients.get(0))).thenReturn(client);
        assertEquals(new ResponseEntity<>(client, HttpStatus.OK), clientController.updateClient(1L, client, clientAuth));
    }

}
