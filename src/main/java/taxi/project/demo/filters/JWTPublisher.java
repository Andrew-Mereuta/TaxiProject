package taxi.project.demo.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import taxi.project.demo.entities.Client;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTPublisher extends UsernamePasswordAuthenticationFilter {

    private CreatJWT tokenConstruct = new CreatJWT();
    private final AuthenticationManager authenticationManager;

    public JWTPublisher(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Client authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), Client.class);

            Authentication authenticate = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()
            );

            Authentication auth = authenticationManager.authenticate(authenticate);
            return auth;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Client client = (Client) authResult.getPrincipal();
        String token = tokenConstruct.createJWT(client);

        response.setHeader(HttpHeaders.AUTHORIZATION, token);
    }
}
