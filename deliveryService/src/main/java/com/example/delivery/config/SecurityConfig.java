package com.example.delivery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // 🧍 REGISTER (OPEN)
                        .requestMatchers(HttpMethod.POST, "/delivery/register")
                        .permitAll()

                        // 🔍 VIEW NEARBY AGENTS (CUSTOMER + ADMIN + USER)
                        .requestMatchers(HttpMethod.GET, "/delivery/nearby")
                        .hasAnyAuthority("CUSTOMER", "ADMIN", "USER")

                        // 📍 LOCATION UPDATE (DELIVERY ONLY)
                        .requestMatchers(HttpMethod.PUT, "/delivery/location")
                        .hasAuthority("DELIVERY")

                        // 🟢 AVAILABILITY
                        .requestMatchers(HttpMethod.PUT, "/delivery/availability")
                        .hasAuthority("DELIVERY")

                        // 🔐 VERIFY AGENT (ADMIN ONLY)
                        .requestMatchers("/delivery/verify/**")
                        .hasAuthority("ADMIN")

                        // 📦 ASSIGN ORDER
                        .requestMatchers("/delivery/assign")
                        .hasAnyAuthority("ADMIN", "SYSTEM", "CUSTOMER", "USER")

                        // 🚚 COMPLETE DELIVERY
                        .requestMatchers("/delivery/complete/**")
                        .hasAuthority("DELIVERY")

                        // ⭐ RATING
                        .requestMatchers("/delivery/rating")
                        .hasAuthority("CUSTOMER")

                        // 📊 ACTIVE DELIVERIES
                        .requestMatchers("/delivery/active/**")
                        .hasAuthority("DELIVERY")

                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}