package com.saberdot.photoapp.api.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private Environment env;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())  // Disable CSRF for stateless API
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.POST, "/Photo-App-Api-Users/users").permitAll()  // Allow POST /users without JWT
                        .pathMatchers(HttpMethod.POST, "/Photo-App-Api-Users/users/login").permitAll()  // Allow POST /users/login without JWT
                        .pathMatchers(HttpMethod.GET, "/Photo-App-Api-Users/users/**").authenticated()  // Require JWT for GET requests
                        .pathMatchers(HttpMethod.PUT, "/Photo-App-Api-Users/users/**").authenticated()  // Require JWT for PUT requests
                        .pathMatchers(HttpMethod.DELETE, "/Photo-App-Api-Users/users/**").authenticated()  // Require JWT for DELETE requests
                        .anyExchange().authenticated()  // Require JWT for any other requests
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.jwtDecoder(reactiveJwtDecoder())))
                .build();
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        String secretKey = env.getProperty("token.secret");
        byte[] secretKeyBytes = Base64.getDecoder().decode(secretKey);
        return NimbusReactiveJwtDecoder.withSecretKey(Keys.hmacShaKeyFor(secretKeyBytes)).build();
    }
}
