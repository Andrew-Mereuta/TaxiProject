package taxi.project.demo.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import taxi.project.demo.entities.Car;
import taxi.project.demo.entities.Client;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.entities.Order;

import java.io.IOException;

public class OrderSerializer extends StdSerializer<Order> {

    protected OrderSerializer(Class<Order> t) {
        super(t);
    }

    public OrderSerializer() {
        this(null);
    }

    @Override
    public void serialize(Order order, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        Client client = order.getClient();
        Driver driver = order.getDriver();
        Car car = driver.getCar();
        jgen.writeStartObject();
        jgen.writeNumberField("price", order.getPrice());
        jgen.writeStringField("clientEmail", client.getEmail());
        jgen.writeStringField("driverEmail", driver.getEmail());
        jgen.writeStringField("car", car.getModel());
        jgen.writeEndObject();
    }
}
