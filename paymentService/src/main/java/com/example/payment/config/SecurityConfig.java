package com.example.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    	http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth

            // 💳 CREATE RAZORPAY ORDER
            .requestMatchers(HttpMethod.POST, "/payment/create-order")
            .hasAnyAuthority("CUSTOMER", "USER")

            // ✅ VERIFY PAYMENT (THIS WAS MISSING)
            .requestMatchers(HttpMethod.POST, "/payment/verify")
            .hasAnyAuthority("CUSTOMER", "USER")

            // 💰 WALLET
            .requestMatchers(HttpMethod.POST, "/payment/wallet/**")
            .hasAnyAuthority("CUSTOMER", "USER")

            // 📊 VIEW PAYMENTS
            .requestMatchers("/payment/customer/**")
            .hasAnyAuthority("CUSTOMER", "ADMIN", "USER")
            
            // 🔥 ADD THIS (VERY IMPORTANT)
            .requestMatchers("/payment/order/**")
            .hasAnyAuthority("CUSTOMER", "ADMIN", "USER")

            // 🔁 REFUND
            .requestMatchers("/payment/refund/**")
            .hasAuthority("ADMIN")

            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
    }
}
