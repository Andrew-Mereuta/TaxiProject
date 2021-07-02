package taxi.project.demo.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CarTest {

    private Car car = new Car();
    private Client client = new Client();
    private Driver driver = new Driver();


    @BeforeEach
    void setUp() {
        car.setId(1L);
        car.setModel("BMW");
        car.setDriver(driver);

        client.setId(1L);
        client.setOrders(new ArrayList<>());
        client.setRole("ROLE_CLIENT");
        client.setEmail("andrew@email.com");
        client.setPassword("password");
        client.setName("Andrew");

        driver.setId(1L);
        driver.setEmail("driver@email.com");
        driver.setCar(car);
        driver.setBusy(false);
        driver.setOrders(new ArrayList<>());
        driver.setPassword("password");
        driver.setRole("ROLE_DRIVER");
        driver.setName("Mr.Rock");
    }

    @Test
    public void equalsTest1() {
        assertEquals(car, car);
    }

    @Test
    public void equalsTest2() {
        assertNotEquals(car, null);
    }

    @Test
    public void equalsTest3() {
        Car car2 = new Car(2L, "BMW", driver);
        assertNotEquals(car, car2);
    }

    @Test
    public void equalsTest4() {
        Car car2 = new Car(1L, "BMW", driver);
        assertEquals(car, car2);
    }

    @Test
    public void equalsTest5() {
        Car car2 = new Car(1L, "BMW", new Driver());
        assertNotEquals(car, car2);
    }
}
