package taxi.project.demo.serializers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taxi.project.demo.entities.Car;
import taxi.project.demo.entities.Driver;

import java.io.IOException;
import java.io.StringWriter;

public class CarSerializerTest {

    private final CarSerializer carSerializer = new CarSerializer();

    private final Car car = new Car();
    private final Driver driver = new Driver();

    @BeforeEach
    public void setUp() {
        driver.setName("Driver-Name");
        driver.setPassword("password");
        driver.setRole("ROLE_DRIVER");
        driver.setEmail("driver@email.com");
        driver.setId(1L);

        car.setModel("Ferrari");
        car.setDriver(driver);
    }

    @Test
    public void serializeTest1() {
        Assertions.assertNotNull(carSerializer);
    }

    @Test
    public void serializeTest2() throws IOException {
        JsonFactory factory = new JsonFactory();
        StringWriter jsonObjectWriter = new StringWriter();
        JsonGenerator jgen = factory.createGenerator(jsonObjectWriter);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        carSerializer.serialize(car, jgen, provider);
        Assertions.assertNotNull(carSerializer);
    }

}
