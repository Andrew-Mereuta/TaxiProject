package taxi.project.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import taxi.project.demo.entities.Client;
import taxi.project.demo.enums.Role;
import taxi.project.demo.repositories.ClientRepository;

@Service
public class ClientService implements UserDetailsService {

    private ClientRepository clientRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public ClientService(ClientRepository clientRepository,
                         BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.clientRepository = clientRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public int registerClient(Client client) {
        Client c = (Client) loadUserByUsername(client.getEmail());
        if(client.getPassword().isEmpty()
                || client.getEmail().isEmpty()
                || client.getName().isEmpty()
                || loadUserByUsername(client.getEmail()) != null
                || client.getPassword().trim().length() < 6) {
            return 405;
        }
        client.setRole(Role.CLIENT);
        client.setPassword(bCryptPasswordEncoder.encode(client.getPassword()));
        clientRepository.save(client);
        return 201;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmail(email);
        return client;
    }
}
