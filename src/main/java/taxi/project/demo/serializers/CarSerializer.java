package taxi.project.demo.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import taxi.project.demo.entities.Car;
import taxi.project.demo.entities.Driver;

import java.io.IOException;

public class CarSerializer extends StdSerializer<Car> {

    protected CarSerializer(Class<Car> t) {
        super(t);
    }

    public CarSerializer() {
        this(null);
    }

    @Override
    public void serialize(Car car, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        Driver driver = car.getDriver();
        jgen.writeStartObject();
        jgen.writeStringField("model", car.getModel());
        jgen.writeStringField("driver-email", driver.getEmail());
        jgen.writeStringField("driver-name", driver.getName());
        jgen.writeEndObject();
    }
}
