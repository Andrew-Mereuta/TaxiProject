package taxi.project.demo.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import taxi.project.demo.entities.Driver;

import java.io.IOException;

public class DriverSerializer extends StdSerializer<Driver> {

    protected DriverSerializer(Class<Driver> t) {
        super(t);
    }

    public DriverSerializer() {
        this(null);
    }

    @Override
    public void serialize(Driver driver, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("email", driver.getEmail());
        jgen.writeStringField("name", driver.getName());
        jgen.writeStringField("role", driver.getRole().toString());
        jgen.writeStringField("isBusy", String.valueOf(driver.isBusy()));
        jgen.writeEndObject();
    }
}
