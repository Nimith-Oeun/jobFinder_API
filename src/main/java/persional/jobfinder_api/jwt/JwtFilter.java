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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import persional.jobfinder_api.dto.request.LoginRequest;
import persional.jobfinder_api.dto.respones.LoginRespone;
import persional.jobfinder_api.exception.InternalServerError;
import persional.jobfinder_api.model.UserProfile;
import persional.jobfinder_api.repository.UserProfileRepository;
import persional.jobfinder_api.utils.JwtSecretUtil;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.Key;
import java.time.LocalDate;
import java.util.Date;

@RequiredArgsConstructor
public class JwtFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserProfileRepository userProfileRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {

        ObjectMapper mapper = new ObjectMapper();

        try {

            LoginRequest loginRequest = mapper.readValue(
                    request.getInputStream(),
                    LoginRequest.class
            );

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );
            return authenticationManager.authenticate(authentication);

        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerError("Error while authenticating user");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {

        // Generate JWT token
        String token = Jwts.builder()
                .subject(authResult.getName())
                .issuedAt(new Date())
                .claim("authorities", authResult.getAuthorities())
                .expiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // 30 minute
                .issuer("jobfinder_api")
                .signWith(JwtSecretUtil.getSecretKey())
                .compact();

        // Refresh Token (long expiry)
        String refreshToken = Jwts.builder()
                .subject(authResult.getName())
                .issuedAt(new Date())
                .claim("authorities", authResult.getAuthorities())
                .expiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 7 days
                .issuer("jobfinder_api")
                .signWith(JwtSecretUtil.getSecretKey())
                .compact();

        // Response object to hold the login response
        UserProfile profile = userProfileRepository.findByUsername(authResult.getName()).get();
        LoginRespone loginRespone = LoginRespone.builder()
                .userName(profile.getUsername())
                .token(token)
                .refreshToken(refreshToken)
                .authorities(
                        authResult.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .filter(auth -> auth.startsWith("ROLE_"))
                                .toArray(String[]::new)
                )
                .build();

        // Write the LoginResponse object to the response body
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(loginRespone));

        // Set the token in the response header
        response.setHeader("Authorization","Bearer " + token);
    }
}
