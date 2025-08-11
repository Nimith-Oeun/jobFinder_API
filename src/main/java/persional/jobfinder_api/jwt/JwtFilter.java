package persional.jobfinder_api.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import persional.jobfinder_api.dto.request.LoginRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;


@RequiredArgsConstructor
public class JwtFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Value("${JWT_SECRETE_KEY}")
    String secretKey;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {

        ObjectMapper mapper = new ObjectMapper();

        try {

            LoginRequest loginRequest = mapper.readValue(request.getInputStream(), LoginRequest.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
            return authenticationManager.authenticate(authentication);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while authenticating user");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {

        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .setIssuedAt(new Date())
                .claim("authorities", authResult.getAuthorities())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1))) // 1 day expiration
                .setIssuer("jobfinder_api")
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        response.setHeader("Authorization", token);
    }
}
