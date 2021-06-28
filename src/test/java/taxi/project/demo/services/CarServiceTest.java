package taxi.project.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import taxi.project.demo.entities.Car;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.repositories.CarRepository;
import taxi.project.demo.repositories.DriverRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CarServiceTest {

    private final CarRepository carRepository = Mockito.mock(CarRepository.class);
    private final DriverRepository driverRepository = Mockito.mock(DriverRepository.class);
    private final CarService carService = new CarService(carRepository, driverRepository);
    private Driver driver = new Driver(1L, "Mr.Rock", "rock@mail.com",
            "password", "ROLE_DRIVER", false,
            null, null);
    private Car car = new Car(1L, "Ferrari", driver);

    @BeforeEach
    void setUp() {
        car.setDriver(driver);
    }

    @Test
    public void findCarByIdTest1() {
        when(carRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(car));
        assertEquals(car, carService.findCarById(1L));
    }

    @Test
    public void findCarByIdTest2() {
        when(carRepository.findById(2L)).thenReturn(java.util.Optional.empty());
        assertNull(carService.findCarById(1L));
    }

    @Test
    public void findCarByDriverIdTest1() {
        when(carRepository.findByDriver_Id(1L)).thenReturn(java.util.Optional.ofNullable(car));
        assertEquals(car, carService.findCarByDriverId(1L));
    }

    @Test
    public void findCarByDriverIdTest2() {
        when(carRepository.findByDriver_Id(2L)).thenReturn(java.util.Optional.empty());
        assertNull(carService.findCarByDriverId(2L));
    }

    @Test
    public void filterByModelTest() {
        when(carRepository.findAllByModel("Ferrari")).thenReturn(List.of(car));
        assertArrayEquals(List.of(car).toArray(), carService.filterByModel("Ferrari").toArray());
    }

    @Test
    public void findAllCarsTest() {
        when(carRepository.findAll()).thenReturn(List.of(car));
        assertArrayEquals(List.of(car).toArray(), carService.findAllCars().toArray());
    }

    @Test
    public void updateCarTest() {
        Driver d = new Driver();
        d.setName("New driver");
        d.setEmail("New email");
        Car car1 = new Car();
        car1.setModel("BMW");

        when(carRepository.save(any(Car.class)))
                .thenReturn(new Car(1L, "BMW", d));

        assertEquals(d, carService.updateCar(car, car1, d).getDriver());
        assertEquals(car1.getModel(), carService.updateCar(car, car1, d).getModel());
    }

    @Test
    public void changeModelTest() {
        assertEquals("Mers", carService.changeModel(car, "Mers").getModel());
        verify(carRepository, times(1)).updateModel(anyString(), anyLong());
    }

    @Test
    public void deleteCarTest() {
        when(carRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(car));
        carService.deleteCar(1L);
        verify(driverRepository, times(1)).deleteById(1L);
    }
}
