package com.project.e_commerce.configurations;

import com.project.e_commerce.filters.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/v1/users/register", "/api/v1/users/login").permitAll()
                .requestMatchers("/api/v1/products/**").permitAll()
                    .requestMatchers("/api/v1/categories/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
            
        return http.build();
    }
}
