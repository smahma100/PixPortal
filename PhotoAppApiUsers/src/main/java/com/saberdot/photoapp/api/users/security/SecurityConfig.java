package com.saberdot.photoapp.api.users.security;

import com.saberdot.photoapp.api.users.service.UsersService;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsersService usersService;  // Inject UsersService
    private final Environment env;  // Inject Environment

    @Autowired
    public SecurityConfig(UsersService usersService, Environment env) {
        this.usersService = usersService;
        this.env = env;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Create the custom AuthenticationFilter and configure its login URL
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(usersService, env, authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)));
        authenticationFilter.setFilterProcessesUrl("/users/login");

        http
                .csrf(csrf -> csrf.disable())  // Disable CSRF for API
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()  // Allow POST /users without JWT
                        .requestMatchers(HttpMethod.POST, "/users/login").permitAll()  // Allow POST /users/login without JWT
                        .requestMatchers(HttpMethod.GET, "/users/**").authenticated()  // Require JWT for GET requests
                        .requestMatchers(HttpMethod.PUT, "/users/**").authenticated()  // Require JWT for PUT requests
                        .requestMatchers(HttpMethod.DELETE, "/users/**").authenticated()  // Require JWT for DELETE requests
                        .anyRequest().authenticated()  // All other requests require authentication
                )
                .addFilter(authenticationFilter)  // Add the custom authentication filter
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())))  // Configure JWT validation
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));  // Stateless session management

        // Return the built SecurityFilterChain object
        return http.build();
    }

    // Provide AuthenticationManager bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // JwtDecoder for decoding the JWT tokens
    @Bean
    public JwtDecoder jwtDecoder() {
        String secretKey = env.getProperty("token.secret");
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    // Customize security to ignore requests to H2 Console
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/h2-console/**");
    }
}
