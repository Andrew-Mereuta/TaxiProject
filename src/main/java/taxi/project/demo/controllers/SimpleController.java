package taxi.project.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taxi.project.demo.entities.Client;
import taxi.project.demo.exceptions.TokenException;
import taxi.project.demo.repositories.ClientRepository;
import taxi.project.demo.serializers.ClientSerializer;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class SimpleController {

    private ObjectMapper mapper = new ObjectMapper();
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
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public String allStudents() throws JsonProcessingException {
        List<Client> clients = clientRepository.findAll();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Client.class, new ClientSerializer());
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.registerModule(module);

        String result = mapper.writeValueAsString(clients);
        return result;
    }

}
