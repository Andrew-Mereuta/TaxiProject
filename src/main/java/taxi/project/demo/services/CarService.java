package taxi.project.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taxi.project.demo.entities.Car;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.repositories.CarRepository;

import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car findCarById(Long carId){
        return carRepository.findById(carId).orElse(null);
    }

    public Car findCarByDriverId(Long driverId){
        return carRepository.findByDriver_Id(driverId).orElse(null);
    }

    public List<Car> filterByModel(String model) {
        return carRepository.findAllByModel(model);
    }

    public List<Car> findAllCars(){
        return carRepository.findAll();
    }

    public Car updateCar(Car current, Car car, Driver driver) {
        current.setDriver(driver);
        current.setModel(car.getModel());
        return carRepository.save(current);
    }

    public Car changeModel(Car carById, String model) {
        carRepository.updateModel(model, carById.getId());
        carById.setModel(model);
        return carById;
    }

    public void deleteCar(Long carId) {
        carRepository.deleteById(carId);
    }
}
