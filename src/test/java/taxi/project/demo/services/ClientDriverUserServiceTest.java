package taxi.project.demo.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import taxi.project.demo.entities.Client;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.exceptions.ResourceNotFoundException;
import taxi.project.demo.repositories.ClientRepository;
import taxi.project.demo.repositories.DriverRepository;

import static org.mockito.Mockito.when;

public class ClientDriverUserServiceTest {

    private final ClientRepository clientRepository = Mockito.mock(ClientRepository.class);
    private final DriverRepository driverRepository = Mockito.mock(DriverRepository.class);
    private final ClientDriverUserService userService = new ClientDriverUserService(
            clientRepository,
            driverRepository
    );

    private Client client = new Client();
    private Driver driver = new Driver();

    private final String clientEmail = "client@email.com";
    private final String driverEmail = "driver@email.com";

    @BeforeEach
    public void setUp() {
        client.setName("client");
        client.setPassword("pass");
        client.setRole("ROLE_CLIENT");
        client.setEmail(clientEmail);
        client.setId(1L);

        driver.setName("driver");
        driver.setPassword("pass");
        driver.setRole("ROLE_DRIVER");
        driver.setEmail(driverEmail);
        driver.setId(1L);
        driver.setBusy(true);
    }

    @Test
    public void loadUserByUsernameTest1() {
        when(clientRepository.findByEmail(clientEmail)).thenReturn(client);
        when(driverRepository.findByEmail(clientEmail)).thenReturn(null);
        Assertions.assertEquals(client, userService.loadUserByUsername(clientEmail));
    }

    @Test
    public void loadUserByUsernameTest2() {
        when(clientRepository.findByEmail(driverEmail)).thenReturn(null);
        when(driverRepository.findByEmail(driverEmail)).thenReturn(driver);
        Assertions.assertEquals(driver, userService.loadUserByUsername(driverEmail));
    }

    @Test
    public void loadUserByUsernameTest3() {
        when(clientRepository.findByEmail("random@email.com")).thenReturn(null);
        when(driverRepository.findByEmail("random@email.com")).thenReturn(null);
        Assertions.assertThrows(ResourceNotFoundException.class, ()->{
            userService.loadUserByUsername("random@email.com");
        });
    }
}
