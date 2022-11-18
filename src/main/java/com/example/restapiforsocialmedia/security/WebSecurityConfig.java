package com.example.restapiforsocialmedia.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthFilter customAuthFilter = new CustomAuthFilter(authenticationManager());
        customAuthFilter.setFilterProcessesUrl("/login");
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests((auth) -> {
                    auth.antMatchers("/login").permitAll()
                            .antMatchers(HttpMethod.POST,"/api/v1/users").permitAll()
                            .antMatchers("/api/v1/admin/**").hasRole("ADMIN")
                            .antMatchers("/db").hasRole("ADMIN")
                            .anyRequest().authenticated()
                            .and()
                            .addFilter(customAuthFilter)
                            .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
                });

        return http.build();
    }
    @Bean
    protected PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration().getAuthenticationManager();
    }
    @Bean
    AuthenticationConfiguration authenticationConfiguration() {
        AuthenticationConfiguration authenticationConfiguration;
        authenticationConfiguration = new AuthenticationConfiguration();
        return authenticationConfiguration;
    }
}

