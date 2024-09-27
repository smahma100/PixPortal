package com.saberdot.photoapp.api.users.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saberdot.photoapp.api.users.service.UsersService;
import com.saberdot.photoapp.api.users.shared.UserDto;
import com.saberdot.photoapp.api.users.ui.model.LoginRequestModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UsersService usersService;
    private Environment environment;

    public AuthenticationFilter(UsersService usersService, Environment environment,
                                AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.usersService = usersService;
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {

            LoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequestModel.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String userName = ((User) auth.getPrincipal()).getUsername();
        UserDto userDetails = usersService.getUserDetailsByEmail(userName);
        String tokenSecret = environment.getProperty("token.secret");
       // byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        byte[] secretKeyBytes = Base64.getDecoder().decode(tokenSecret);
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        Instant now = Instant.now();

        String token = Jwts.builder()
                .subject(userDetails.getUserId())
               // .expiration(Date.from(now.plusMillis(Long.parseLong(Objects.requireNonNull(environment.getProperty("token.expiration_time"))))))
                .expiration(Date.from(now.plusMillis(Long.parseLong(environment.getProperty("token.expiration_time")))))
                .issuedAt(Date.from(now))
                .signWith(secretKey)
                .compact();

        res.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        res.addHeader("userId", userDetails.getUserId());
    }
}