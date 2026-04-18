package com.sbi.lms.config;

import com.sbi.lms.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * DevSecOps Security Configuration.
 *
 * Covers:
 *   A01 Broken Access Control    — @EnableMethodSecurity + @PreAuthorize on controllers
 *   A02 Cryptographic Failures   — BCryptPasswordEncoder cost 12
 *   A05 Security Misconfiguration — restricted CORS, security headers, stateless sessions
 *
 * Lab 2 (DAST): ZAP will flag missing/misconfigured security headers.
 * After this config is applied the following headers appear on all responses:
 *   Content-Security-Policy, Strict-Transport-Security, X-Frame-Options, X-Content-Type-Options
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity          // enables @PreAuthorize on controller methods
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Stateless — no session cookies (JWT-based API)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.disable()) // REST API — CSRF not applicable; ZAP will note this
            .cors(cors -> cors.configurationSource(corsConfig()))

            // Security response headers — Lab 2: ZAP scans for these
            .headers(headers -> headers
                .contentSecurityPolicy(csp ->
                    csp.policyDirectives("default-src 'self'; frame-ancestors 'none'"))
                // sameOrigin (not deny) so H2 console works in the lab environment
                // In production: use deny()
                .frameOptions(frame -> frame.sameOrigin())
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000))
            )

            // Public endpoints — auth + OpenAPI (needed for ZAP auto-discovery) + H2 console
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/v1/auth/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/actuator/health",
                    "/actuator/info",
                    "/h2-console/**"
                ).permitAll()
                .anyRequest().authenticated()
            )

            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfig() {
        CorsConfiguration cfg = new CorsConfiguration();
        // DevSecOps: restrict to known origins — never use "*" in banking apps
        cfg.setAllowedOrigins(List.of("https://lms.sbi.internal", "http://localhost:3000"));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        cfg.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", cfg);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // DevSecOps: BCrypt cost factor 12 — never MD5 / SHA-1 / plain text
        return new BCryptPasswordEncoder(12);
    }
}
