package taxi.project.demo.serializers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taxi.project.demo.entities.Client;

import java.io.IOException;
import java.io.StringWriter;

public class ClientSerializerTest {

    private final ClientSerializer clientSerializer = new ClientSerializer();

    private Client client = new Client();

    @BeforeEach
    public void setUp() {
        client.setName("client");
        client.setPassword("pass");
        client.setRole("ROLE_CLIENT");
        client.setEmail("client@email.com");
        client.setId(1L);
    }

    @Test
    public void serializeTest1() {
        Assertions.assertNotNull(clientSerializer);
    }

    @Test
    public void serializeTest2() throws IOException {
        JsonFactory factory = new JsonFactory();
        StringWriter jsonObjectWriter = new StringWriter();
        JsonGenerator jgen = factory.createGenerator(jsonObjectWriter);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        clientSerializer.serialize(client, jgen, provider);
        Assertions.assertNotNull(clientSerializer);
    }
}
