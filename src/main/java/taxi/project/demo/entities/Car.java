package taxi.project.demo.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import taxi.project.demo.serializers.CarSerializer;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "car")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = CarSerializer.class)
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String model;

    @OneToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(id, car.id)
                && Objects.equals(model, car.model)
                && Objects.equals(driver.getId(), car.driver.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model, driver);
    }
}
