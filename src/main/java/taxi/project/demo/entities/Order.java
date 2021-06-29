package taxi.project.demo.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import taxi.project.demo.serializers.OrderSerializer;

import javax.persistence.*;

@Entity
@Table(name="order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonSerialize(using = OrderSerializer.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private Client client;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "driver_id", referencedColumnName = "driver_id")
    private Driver driver;

    private double price;
}
