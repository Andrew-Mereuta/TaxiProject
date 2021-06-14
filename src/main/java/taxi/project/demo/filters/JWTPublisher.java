package taxi.project.demo.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import taxi.project.demo.entities.Client;
import taxi.project.demo.entities.Driver;
import taxi.project.demo.services.ClientService;
import taxi.project.demo.services.DriverService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

public class JWTPublisher extends UsernamePasswordAuthenticationFilter {

    private CreatJWT tokenConstruct = new CreatJWT();
    private final AuthenticationManager authenticationManager;
    private final ClientService clientService;
    private final DriverService driverService;

    public JWTPublisher(AuthenticationManager authenticationManager,
                        ClientService clientService, DriverService driverService) {
        this.authenticationManager = authenticationManager;
        this.clientService = clientService;
        this.driverService = driverService;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Client authenticationClientRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), Client.class);

//            Driver authenticationDriverRequest = new ObjectMapper()
//                    .readValue(request.getInputStream(), Driver.class);
//
//


            Authentication authenticate = new UsernamePasswordAuthenticationToken(
                    authenticationClientRequest.getEmail(),
                    authenticationClientRequest.getPassword()
            );

            Authentication auth = authenticationManager.authenticate(authenticate);
            return auth;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String name = authResult.getPrincipal().getClass().getName().toLowerCase();
        String token;
        if(name.contains("client")) {
            Client client = (Client) authResult.getPrincipal();
            token = tokenConstruct.createJWT(client);
        } else {
            Driver driver = (Driver) authResult.getPrincipal();
            token = tokenConstruct.createJWT(driver);
        }

        response.setHeader(HttpHeaders.AUTHORIZATION, token);
    }
}
