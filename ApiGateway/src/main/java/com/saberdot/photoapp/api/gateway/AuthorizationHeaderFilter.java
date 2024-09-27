package com.saberdot.photoapp.api.gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Base64;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final Environment env;

    @Autowired
    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }

    public static class Config {
        // Configuration properties can be added here if needed
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            // Check if the Authorization header is present
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
            }

            // Extract the JWT token from the header
            String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            String jwt = authorizationHeader.replace("Bearer", "").trim();

            // Validate the JWT token
            if (!isJwtValid(jwt)) {
                return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
            }

            // Proceed with the request
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String errMsg, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        // Optionally, you can add a response body or headers here
        return response.setComplete();
    }

    private boolean isJwtValid(String jwt) {
        try {
            // Retrieve the secret key from the environment
            String tokenSecret = env.getProperty("token.secret");
            byte[] secretKeyBytes = Base64.getDecoder().decode(tokenSecret);
            SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

            // Parse and validate the JWT token using Jwts.parserBuilder() with setSigningKey
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            String subject = claims.getSubject();

            // Check if the subject is present and not empty
            return (subject != null && !subject.isEmpty());
        } catch (Exception ex) {
            // Token validation failed
            return false;
        }
    }
}