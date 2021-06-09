package taxi.project.demo.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import taxi.project.demo.entities.Client;

import java.io.IOException;

public class ClientSerializer extends StdSerializer<Client> {

    protected ClientSerializer(Class<Client> t) {
        super(t);
    }

    public ClientSerializer() {
        this(null);
    }

    @Override
    public void serialize(Client client, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("name", client.getName());
        jgen.writeStringField("email", client.getEmail());
        jgen.writeEndObject();
    }
}
//how to use. Guide for future.
//    Item myItem = new Item(1, "theItem", new User(2, "theUser"));
//    ObjectMapper mapper = new ObjectMapper();
//
//    SimpleModule module = new SimpleModule();
//    module.addSerializer(Item.class, new ItemSerializer());
//    mapper.registerModule(module);
//
//    String serialized = mapper.writeValueAsString(myItem);