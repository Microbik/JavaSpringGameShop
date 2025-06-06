package com.example.gameplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/purchases/**").hasAnyRole("Player", "ADMIN")
                        .requestMatchers("/api/addons/**").hasAnyRole("GameManager", "ADMIN")
                        .requestMatchers("/api/addon-purchases/**").hasAnyRole("Player", "ADMIN")
                        .requestMatchers("/api/achievements/**").hasAnyRole("GameManager", "ADMIN")
                        .requestMatchers("/api/user-achievements/**").hasAnyRole("GameManager", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/balance/top-up").authenticated()
                        .requestMatchers("api/games/**").hasAnyRole("GameManager", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/games/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/addons/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/genres/**").permitAll()
                        .requestMatchers("/api/genres/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Используется в UserService
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}


