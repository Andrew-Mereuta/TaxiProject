package taxi.project.demo.filters;

import io.jsonwebtoken.Jwts;
import org.hibernate.annotations.Filter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private JWTInfo jwtInfo = new JWTInfo();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if(request.getHeader("Authorization") == null || !request.getHeader("Authorization").contains("Bearer")) {
            filterChain.doFilter(request, response);
        }

        String jwtHeader = request.getHeader("Authorization");
        jwtHeader.replace("Bearer ", "");

        Jwts.parserBuilder()
                .setSigningKey(jwtInfo.getSecretKey())
                .build()
                .parseClaimsJws(jwtHeader);

    }
}
