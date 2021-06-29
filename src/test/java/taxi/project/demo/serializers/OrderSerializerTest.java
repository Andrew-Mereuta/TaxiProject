package taxi.project.demo.serializers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taxi.project.demo.entities.Car;
import taxi.project.demo.entities.Client;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.entities.Order;

import java.io.IOException;
import java.io.StringWriter;

public class OrderSerializerTest {

    private final OrderSerializer orderSerializer = new OrderSerializer();

    private Order order = new Order();
    private Client client = new Client();
    private Driver driver = new Driver();
    private Car car = new Car();

    @BeforeEach
    void setUp() {
        client.setName("client");
        client.setPassword("pass");
        client.setRole("ROLE_CLIENT");
        client.setEmail("client@email.com");
        client.setId(1L);

        driver.setName("driver");
        driver.setPassword("password");
        driver.setRole("ROLE_DRIVER");
        driver.setEmail("driver@email.com");
        driver.setId(1L);

        car.setModel("Ferrari");
        car.setDriver(driver);
        driver.setCar(car);

        order = new Order(1L, client, driver, 74.2);
    }


    @Test
    public void serializeTest1() {
        Assertions.assertNotNull(orderSerializer);
    }

    @Test
    public void serializeTest2() throws IOException {
        JsonFactory factory = new JsonFactory();
        StringWriter jsonObjectWriter = new StringWriter();
        JsonGenerator jgen = factory.createGenerator(jsonObjectWriter);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        orderSerializer.serialize(order, jgen, provider);
        Assertions.assertNotNull(orderSerializer);
    }
}
