package com.example.notification.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.notification.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        System.out.println("NotificationService JwtFilter: Authorization header = " + (header != null ? "Present" : "Null"));

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            System.out.println("NotificationService JwtFilter: Validating token...");

            if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.extractEmail(token);
                String role = jwtUtil.extractRole(token);
                System.out.println("NotificationService JwtFilter: ROLE FROM TOKEN = " + role + ", EMAIL = " + email);

                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        email, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("NotificationService JwtFilter: Authentication set in context.");
            } else {
                System.out.println("NotificationService JwtFilter: Token validation FAILED!");
            }
        }

        filterChain.doFilter(request, response);
    }
}