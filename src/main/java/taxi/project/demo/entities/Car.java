package taxi.project.demo.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import taxi.project.demo.serializers.CarSerializer;
import taxi.project.demo.serializers.ClientSerializer;

import javax.persistence.*;

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
}
