package taxi.project.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import taxi.project.demo.entities.Car;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.enums.Role;
import taxi.project.demo.repositories.DriverRepository;

import java.util.List;

@Service
public class DriverService implements UserDetailsService {

    private DriverRepository driverRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public DriverService(DriverRepository driverRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.driverRepository = driverRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public int saveDriver(Driver driver, Car car) {
        if(driver.getPassword().isEmpty()
                || driver.getEmail().isEmpty()
                || driver.getName().isEmpty()
                || loadUserByUsername(driver.getEmail()) != null
                || driver.getPassword().trim().length() < 6) {
            return 405;
        }

        driver.setCar(car);
        car.setDriver(driver);
        driver.setRole(Role.DRIVER);
        driver.setPassword(bCryptPasswordEncoder.encode(driver.getPassword()));
        driverRepository.save(driver);
        return 201;
    }

    public Driver findDriverById(Long driverId) {
        return driverRepository.findById(driverId).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return driverRepository.findByEmail(email);
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }
}
