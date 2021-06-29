package taxi.project.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import taxi.project.demo.entities.Client;
import taxi.project.demo.repositories.ClientRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ClientServiceTest {

    private final ClientRepository clientRepository = Mockito.mock(ClientRepository.class);
    private final BCryptPasswordEncoder encoder = Mockito.mock(BCryptPasswordEncoder.class);
    private final ClientService clientService = new ClientService(
            clientRepository,
            encoder
    );

    private Client originalClient = new Client();
    private Client admin = new Client();
    private final String clientEmail = "client@email.com";

    @BeforeEach
    public void setUp() {
        originalClient.setName("client");
        originalClient.setPassword("pass");
        originalClient.setRole("ROLE_CLIENT");
        originalClient.setEmail(clientEmail);
        originalClient.setId(1L);

        admin.setName("admin");
        admin.setPassword("admin");
        admin.setRole("ROLE_ADMIN");
        admin.setEmail("admin@email.com");
        admin.setId(2L);
    }

    @ParameterizedTest(name = "client={0}, existingClient={1}, expectedResult={2}")
    @MethodSource("generator")
    public void registerClientTest1(Client client, Client existingClient, int expectedResult) {
        when(clientRepository.findByEmail(anyString())).thenReturn(existingClient);
        int result = clientService.registerClient(client);
        assertEquals(expectedResult, result);
    }

    public static Stream<Arguments> generator() {
        return Stream.of(
            Arguments.of(new Client(1L, "name", "", "email", null, null),
                    null, 405),
            Arguments.of(new Client(1L, "name", "password", "", null, null),
                    null, 405),
            Arguments.of(new Client(1L, "", "password", "email", null, null),
                    null, 405),
            Arguments.of(new Client(1L, "name", "password", "email", null, null),
                    new Client(), 405),
            Arguments.of(new Client(1L, "name", "pass", "email", null, null),
                    null, 405),
            Arguments.of(new Client(1L, "name", "password", "email", null, null),
                    null, 201)
        );
    }

    @Test
    public void loadUserByUsernameTest() {
        when(clientRepository.findByEmail(clientEmail)).thenReturn(originalClient);
        assertEquals(originalClient, clientService.loadUserByUsername(clientEmail));
    }

    @Test
    public void findAllClientsTest() {
        when(clientRepository.findAll()).thenReturn(List.of(originalClient, admin));
        assertArrayEquals(List.of(originalClient).toArray(), clientService.findAllClients().toArray());
    }

    @Test
    public void findClientByIdTest1() {
        when(clientRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(originalClient));
        assertEquals(originalClient, clientService.findClientById(1L));
    }

    @Test
    public void findClientByIdTest2() {
        when(clientRepository.findById(3L)).thenReturn(java.util.Optional.empty());
        assertNull(clientService.findClientById(3L));
    }

    @Test
    public void deleteClientTest1() {
        when(clientRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(originalClient));
        assertTrue(clientService.deleteClient(1L, originalClient));
        verify(clientRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteClientTest2() {
        when(clientRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(originalClient));
        assertTrue(clientService.deleteClient(1L, admin));
        verify(clientRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteClientTest3() {
        when(clientRepository.findById(2L)).thenReturn(java.util.Optional.ofNullable(admin));
        assertFalse(clientService.deleteClient(2L, originalClient));
    }

    @Test
    public void deleteClientTest4() {
        when(clientRepository.findById(3L)).thenReturn(java.util.Optional.empty());
        assertFalse(clientService.deleteClient(3L, admin));
    }

    @Test
    public void updateClientTest() {
        when(encoder.encode(anyString())).thenReturn(admin.getPassword());
        assertEquals(admin.getName(), clientService.updateClient(admin, originalClient).getName());
        assertEquals(admin.getPassword(), clientService.updateClient(admin, originalClient).getPassword());
        verify(clientRepository, times(2)).updateClient(admin.getName(), admin.getPassword(), originalClient.getId());
    }

}
