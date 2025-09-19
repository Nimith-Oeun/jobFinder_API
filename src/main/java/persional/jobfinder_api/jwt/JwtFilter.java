package persional.jobfinder_api.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import persional.jobfinder_api.dto.request.LoginRequest;
import persional.jobfinder_api.dto.respones.LoginRespone;
import persional.jobfinder_api.exception.BadRequestException;
import persional.jobfinder_api.exception.EmptyRespone;
import persional.jobfinder_api.exception.ExceptionResponeDTO;
import persional.jobfinder_api.exception.InternalServerError;
import persional.jobfinder_api.model.UserProfile;
import persional.jobfinder_api.repository.UserProfileRepository;
import persional.jobfinder_api.utils.JwtSecretUtil;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

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

            Optional<UserProfile> byEmail = userProfileRepository.findByEmail(loginRequest.getEmail());

            // Check if email exists
            if (byEmail.isEmpty()) {
                throw new BadRequestException("Invalid email");
            }

            // Check if password matches
            if (!passwordEncoder.matches(loginRequest.getPassword(), byEmail.get().getPassword())) {
                throw new BadRequestException("password is incorrect");
            }


            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );
            return authenticationManager.authenticate(authentication);

        }catch (BadRequestException e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw e; // Rethrow to be handled by the framework

        } catch (Exception e) {
            log.error("Authentication error: {}", e.getMessage());
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
                .expiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // 30 minutes
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
        UserProfile profile = userProfileRepository.findByEmail(authResult.getName()).get();
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
