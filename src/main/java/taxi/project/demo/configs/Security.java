package taxi.project.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import taxi.project.demo.filters.JWTFilter;
import taxi.project.demo.filters.JWTPublisher;
import taxi.project.demo.repositories.ClientRepository;
import taxi.project.demo.services.ClientService;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ClientService clientService;
    private final ClientRepository clientRepository;


    @Autowired
    public Security(BCryptPasswordEncoder bCryptPasswordEncoder,
                    ClientService clientService,
                    ClientRepository clientRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.clientService = clientService;
        this.clientRepository = clientRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JWTPublisher(authenticationManager()))
                .addFilterAfter(new JWTFilter(), JWTPublisher.class)
                .authorizeRequests()
                .antMatchers("/login", "/api/v1/register").permitAll()
                .anyRequest().authenticated();

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(clientService);
        return provider;
    }

}
