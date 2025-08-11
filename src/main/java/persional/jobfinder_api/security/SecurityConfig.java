package persional.jobfinder_api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import persional.jobfinder_api.jwt.JwtFilter;
import persional.jobfinder_api.jwt.TokenVerifyFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final EncrypPassword encrypPassword;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain configur(HttpSecurity http)throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilter(new JwtFilter(authenticationManager(authenticationConfiguration)))
                .addFilterAfter(new TokenVerifyFilter(), JwtFilter.class)
                .sessionManagement(config -> config
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth // use authorizeHttpRequests() to configure authorization
                                .requestMatchers(
                                        "/",
                                        "index.html",
                                        "/jobfinder_api/v1/auth/**",
                                        "api-docs/**",  // OpenAPI docs
                                        "/swagger-ui/**",   // Swagger UI assets
                                        "/swagger-ui.html",
                                        "/api/auth/Register"// Swagger UI page
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                );
        return http.build();
    }

    //this function is used to create an authentication manager
    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
