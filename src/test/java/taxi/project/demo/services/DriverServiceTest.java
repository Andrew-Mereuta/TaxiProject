package taxi.project.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import taxi.project.demo.entities.Car;
import taxi.project.demo.entities.Client;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.repositories.DriverRepository;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DriverServiceTest {

    private final DriverRepository driverRepository = Mockito.mock(DriverRepository.class);
    private final BCryptPasswordEncoder encoder = Mockito.mock(BCryptPasswordEncoder.class);
    private final DriverService driverService = new DriverService(
            driverRepository,
            encoder
    );

    private Driver originalDriver = new Driver();
    private Client admin = new Client();
    private final String driverEmail = "client@email.com";

    @BeforeEach
    public void setUp() {
        originalDriver.setName("driver");
        originalDriver.setPassword("password");
        originalDriver.setRole("ROLE_DRIVER");
        originalDriver.setEmail(driverEmail);
        originalDriver.setId(1L);

        admin.setName("admin");
        admin.setPassword("admin");
        admin.setRole("ROLE_ADMIN");
        admin.setEmail("admin@email.com");
        admin.setId(2L);
    }

    @ParameterizedTest(name = "driver={0}, car={1}, existingDriver={2}, expectedResult={3}")
    @MethodSource("generator")
    public void registerClientTest1(Driver driver, Car car, Driver existingDriver, int expectedResult) {
        when(driverRepository.findByEmail(anyString())).thenReturn(existingDriver);
        int result = driverService.saveDriver(driver, car);
        assertEquals(expectedResult, result);
    }

    public static Stream<Arguments> generator() {
        return Stream.of(
                Arguments.of(new Driver(1L, "name", "email", "", "role", true, null, null),
                        new Car(1L, "model", null), null, 405),
                Arguments.of(new Driver(1L, "name", "", "password", "role", true, null, null),
                        new Car(1L, "model", null), null, 405),
                Arguments.of(new Driver(1L, "", "email", "password", "role", true, null, null),
                        new Car(1L, "model", null), null, 405),
                Arguments.of(new Driver(1L, "name", "email", "password", "role", true, null, null),
                        new Car(1L, "model", null), new Driver(), 405),
                Arguments.of(new Driver(1L, "name", "email", "pass", "role", true, null, null),
                        new Car(1L, "model", null), null, 405),
                Arguments.of(new Driver(1L, "name", "email", "password", "role", true, null, null),
                        new Car(1L, "model", null), null, 201)
        );
    }

    @Test
    public void findDriverByIdTest1() {
        when(driverRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(originalDriver));
        assertEquals(originalDriver, driverService.findDriverById(1L));
    }

    @Test
    public void findDriverByIdTest2() {
        when(driverRepository.findById(3L)).thenReturn(java.util.Optional.empty());
        assertNull(driverService.findDriverById(3L));
    }

    @Test
    public void loadUserByUsernameTest() {
        when(driverRepository.findByEmail(driverEmail)).thenReturn(originalDriver);
        assertEquals(originalDriver, driverService.loadUserByUsername(driverEmail));
    }

    @Test
    public void getAllDriversTest() {
        Driver d = new Driver();
        when(driverRepository.findAll()).thenReturn(List.of(d, originalDriver));
        assertArrayEquals(List.of(d, originalDriver).toArray(), driverService.getAllDrivers().toArray());
    }

    @Test
    public void deleteDriverByIdtest() {
        driverService.deleteDriverById(anyLong());
        verify(driverRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void updateDriver() {
        Driver d = new Driver(1L, "name", "email", "dPassword", "role", true, null, null);
        when(encoder.encode(anyString())).thenReturn(d.getPassword());

        assertEquals(d.getName(), driverService.updateDriver(d, originalDriver).getName());
        assertEquals(d.getPassword(), driverService.updateDriver(d, originalDriver).getPassword());
        verify(driverRepository, times(2)).updateDriver(d.getName(), d.getPassword(), originalDriver.getId());
    }
}

