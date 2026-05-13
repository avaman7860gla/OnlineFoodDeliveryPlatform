package com.example.notification.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // 🔔 SEND NOTIFICATION
                        .requestMatchers(HttpMethod.POST, "/notifications/send")
                        .hasAnyAuthority("ADMIN", "SYSTEM", "CUSTOMER", "DELIVERY")

                        // 🔔 BULK
                        .requestMatchers(HttpMethod.POST, "/notifications/bulk")
                        .hasAuthority("ADMIN")

                        // 👤 USER NOTIFICATIONS
                        .requestMatchers(HttpMethod.GET, "/notifications/**")
                        .hasAnyAuthority("CUSTOMER", "ADMIN", "USER", "PARTNER", "OWNER", "AGENT", "DELIVERY")

                        // ✏️ MARK READ
                        .requestMatchers(HttpMethod.PUT, "/notifications/read/**")
                        .hasAnyAuthority("CUSTOMER", "ADMIN", "USER", "PARTNER", "OWNER", "AGENT", "DELIVERY")

                        // 🗑 DELETE
                        .requestMatchers(HttpMethod.DELETE, "/notifications/**")
                        .hasAuthority("ADMIN")

                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
