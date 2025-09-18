package persional.jobfinder_api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import persional.jobfinder_api.jwt.FilterChainExceptionHandler;
import persional.jobfinder_api.jwt.JwtFilter;
import persional.jobfinder_api.jwt.TokenVerifyFilter;
import persional.jobfinder_api.repository.UserProfileRepository;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true
)
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserProfileRepository userProfileRepository;
    private final FilterChainExceptionHandler filterChainExceptionHandler;

    @Bean
    public SecurityFilterChain configur(HttpSecurity http)throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .addFilter(new JwtFilter(authenticationManager(authenticationConfiguration) , userProfileRepository , passwordEncoder))
                .addFilterBefore(filterChainExceptionHandler, JwtFilter.class)
                .addFilterAfter(new TokenVerifyFilter(), JwtFilter.class)
                .sessionManagement(config -> config
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth // use authorizeHttpRequests() to configure authorization
                                .requestMatchers(
                                        "/",
                                        "index.html",
                                        "/jobfinder_api/v1/auth/**",
                                        "/jobfinder_api/v1/job",
                                        "/jobfinder_api/v1/filter",
                                        "/jobfinder_api/v1/globle-search",
                                        "v3/api-docs/**",  // OpenAPI docs
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

    //this function is used to configure the authentication manager
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(getAuthenticationProvider());
    }

    //this function is used to create an authentication provider
    @Bean
    public AuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

}
