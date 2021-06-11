package taxi.project.demo.filters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.hibernate.annotations.Filter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import taxi.project.demo.exceptions.TokenException;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Set;

public class JWTFilter extends OncePerRequestFilter {

    private JWTInfo jwtInfo = new JWTInfo();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getHeader("Authorization") == null || !request.getHeader("Authorization").contains("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtHeader = request.getHeader("Authorization");
        jwtHeader = jwtHeader.replace("Bearer ", "");

        SecretKey secretKey = Keys.hmacShaKeyFor(jwtInfo.getSecretKey().getBytes());

        Jws<Claims> jws;

        try {
            jws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwtHeader);
            Claims body = jws.getBody();

            String email;
            if(body.containsKey("email")) {
                 email = body.get("email", String.class);
            } else {
                email = "no email";
            }

            String auth;
            if(body.containsKey("role")) {
                auth = body.get("role", String.class);
            } else {
                auth = "no role";
            }
            SimpleGrantedAuthority authorities = new SimpleGrantedAuthority(auth);

            Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, Set.of(authorities));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch(JwtException ex){
            throw new TokenException("Token is not valid");
            //throw new IllegalStateException("Your token is not valid");
        }
        filterChain.doFilter(request, response);
    }
}