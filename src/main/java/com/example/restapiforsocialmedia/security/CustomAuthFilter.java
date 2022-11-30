package com.example.restapiforsocialmedia.security;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.restapiforsocialmedia.user.UserDataLoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;


@Slf4j
public class CustomAuthFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final String secret;


    public CustomAuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.secret = System.getenv("jwt.secret");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserDataLoginDTO userDataLoginDTO;
        try {
            userDataLoginDTO = new ObjectMapper().readValue(request.getInputStream(), UserDataLoginDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String username = userDataLoginDTO.getUsername();
        String password = userDataLoginDTO.getPassword();
        log.info("Attempting authentication for user: "+username  + " with password: "+password);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        try {
            return authenticationManager.authenticate(token);
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", username);
            throw new UsernameNotFoundException("Authentication failed for user: " + username);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        User user = (User) authResult.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1440 * 60 * 1000))
                .withIssuer(request.getRequestURI())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 43200L * 60 * 1000))
                .withIssuer(request.getRequestURI())  
                .sign(algorithm);
        log.info("Generated access token: {}", accessToken);
        response.setHeader("Access-Token", accessToken);
        response.setHeader("Refresh-Token", refreshToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.error("Authentication failed for user: {}", failed.getMessage());
        response.setStatus(401);
    }

}
