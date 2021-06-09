package taxi.project.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import taxi.project.demo.entities.Client;
import taxi.project.demo.repositories.ClientRepository;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class SimpleController {

    private ClientRepository clientRepository;

    @Autowired
    public SimpleController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @PostMapping
    public void createStudent(@RequestBody Client client) {
        clientRepository.save(client);
    }

    @GetMapping("/allClients")
    public List<Client> allStudents() {
        List<Client> clients = clientRepository.findAll();
        return clients;
    }
}
