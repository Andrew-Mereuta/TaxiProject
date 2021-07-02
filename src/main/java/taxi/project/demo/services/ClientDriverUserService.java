package taxi.project.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import taxi.project.demo.entities.Client;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.exceptions.ResourceNotFoundException;
import taxi.project.demo.repositories.ClientRepository;
import taxi.project.demo.repositories.DriverRepository;

@Service
public class ClientDriverUserService implements UserDetailsService {

    private ClientRepository clientRepository;
    private DriverRepository driverRepository;

    @Autowired
    public ClientDriverUserService(ClientRepository clientRepository, DriverRepository driverRepository) {
        this.clientRepository = clientRepository;
        this.driverRepository = driverRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmail(email);
        Driver driver = driverRepository.findByEmail(email);
        if(client != null) {
            return client;
        } else if (driver != null) {
            return driver;
        } else {
            throw new ResourceNotFoundException("Sorry you are not registered in our database");
        }
    }
}
