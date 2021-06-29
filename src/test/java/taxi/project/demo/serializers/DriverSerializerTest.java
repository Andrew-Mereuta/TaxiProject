package taxi.project.demo.serializers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taxi.project.demo.entities.Driver;

import java.io.IOException;
import java.io.StringWriter;

public class DriverSerializerTest {

    private final DriverSerializer driverSerializer = new DriverSerializer();

    private Driver driver = new Driver();

    @BeforeEach
    public void setUp() {
        driver.setName("driver");
        driver.setPassword("password");
        driver.setRole("ROLE_DRIVER");
        driver.setEmail("driver@email.com");
        driver.setId(1L);
    }

    @Test
    public void serializeTest1() {
        Assertions.assertNotNull(driverSerializer);
    }

    @Test
    public void serializeTest2() throws IOException {
        JsonFactory factory = new JsonFactory();
        StringWriter jsonObjectWriter = new StringWriter();
        JsonGenerator jgen = factory.createGenerator(jsonObjectWriter);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        driverSerializer.serialize(driver, jgen, provider);
        Assertions.assertNotNull(driverSerializer);
    }
}
