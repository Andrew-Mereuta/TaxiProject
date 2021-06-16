package taxi.project.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import taxi.project.demo.entities.Client;
import taxi.project.demo.enums.Role;
import taxi.project.demo.repositories.ClientRepository;

@SpringBootApplication
public class TaxiProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaxiProjectApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(ClientRepository repository, BCryptPasswordEncoder encoder) {
        Client admin = new Client();
        admin.setName("Admin");
        admin.setPassword(encoder.encode("admin"));
        admin.setEmail("admin@email.com");
        admin.setRole("ROLE_ADMIN");
        return (args) -> {
            repository.save(admin);
        };
    }

}
