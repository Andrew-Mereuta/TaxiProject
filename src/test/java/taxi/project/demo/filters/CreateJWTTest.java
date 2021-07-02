package taxi.project.demo.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taxi.project.demo.entities.Car;
import taxi.project.demo.entities.Client;
import taxi.project.demo.entities.Driver;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateJWTTest {

    private final String jwtClient = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoiQW5kcmV3IiwiZW1haWwiOiJhbmRyZXdAZW1haWwuY29tIiwicm9sZSI6IlJPTEVfQ0xJRU5UIn0.Fpb-NlXKLx7euKGRvZ_BaH20G9UtVUvo9UmicJ-7WU-TOFoqBBVmRInhUKRjFrru-Se64t9-oAGv9dj9O9v_iw";
    private final String jwtDriver = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoiTXIuUm9jayIsImVtYWlsIjoiZHJpdmVyQGVtYWlsLmNvbSIsInJvbGUiOiJST0xFX0RSSVZFUiIsImlzQnVzeSI6ZmFsc2UsImNhck1vZGVsIjoiQk1XIn0.tmStsGPMMsaJ7bNy2JIgSRkXRFq_jbjX1kzHbaWpWOy8c0yAR7j-lWic0j_Dr_e1J_jxfasA7wWM5_P2gqEUHg";
    private final CreatJWT create = new CreatJWT();
    private final Client client = new Client();
    private final Driver driver = new Driver();


    @BeforeEach
    void setUp() {
        client.setId(1L);
        client.setOrders(new ArrayList<>());
        client.setRole("ROLE_CLIENT");
        client.setEmail("andrew@email.com");
        client.setPassword("password");
        client.setName("Andrew");

        driver.setId(1L);
        driver.setEmail("driver@email.com");
        driver.setCar(new Car(1L, "BMW", driver));
        driver.setBusy(false);
        driver.setOrders(new ArrayList<>());
        driver.setPassword("password");
        driver.setRole("ROLE_DRIVER");
        driver.setName("Mr.Rock");
    }


    @Test
    public void createJWTClientTest() {
        assertEquals(jwtClient, create.createJWT(client));
    }

    @Test
    public void createJWTDriverTest() {
        assertEquals(jwtDriver, create.createJWT(driver));
    }
}
