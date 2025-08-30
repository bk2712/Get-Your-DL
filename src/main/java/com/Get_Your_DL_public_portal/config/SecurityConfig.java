package com.Get_Your_DL_public_portal.config;

import com.Get_Your_DL_public_portal.filter.JWTAuthenticationFilter;
import com.Get_Your_DL_public_portal.service.UserServiceImpl;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;


@Component
@EnableWebSecurity  // enable spring web security
// it registers SecurityFilterChain to intercept all the http requests
public class SecurityConfig {
    @Autowired
    JWTAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    UserServiceImpl userDetsServiceImpl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {})  // Enable global Spring CORS configuration
                .csrf(AbstractHttpConfigurer::disable) // makes STATEless apis, server do not store client information
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/user/**", "/api/v1/my-profile/**").permitAll()
                        .requestMatchers("/users/fill-details", "/api/v1/my-profile", "/api/v1/my-profile/**", "/api/v1/files/upload", "/api/v1/files/get/**").authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // this add jwtAuthenticationFilter before UsernamePasswordAuthenticationFilter

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
