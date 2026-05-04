package com.hrms.config;

import com.hrms.security.JwtAuthenticationEntryPoint;
import com.hrms.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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

import java.util.Arrays;

/**
 * Spring Security Configuration
 * - JWT authentication
 * - Role-based access control
 * - CORS configuration
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .exceptionHandling(exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(authz ->
                authz
                    // Public endpoints
                    .requestMatchers("/api/v1/auth/**").permitAll()
                    .requestMatchers("/api/v1/docs/**").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/actuator/health").permitAll()
                    // Employee endpoints - accessible by EMPLOYEE, HR_MANAGER, ADMIN
                    .requestMatchers(HttpMethod.GET, "/api/v1/employees").hasAnyRole("EMPLOYEE", "HR_MANAGER", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v1/employees/**").hasAnyRole("EMPLOYEE", "HR_MANAGER", "ADMIN")
                    // HR endpoints - accessible by HR_MANAGER, ADMIN
                    .requestMatchers(HttpMethod.POST, "/api/v1/employees").hasAnyRole("HR_MANAGER", "ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/v1/employees/**").hasAnyRole("HR_MANAGER", "ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/employees/**").hasRole("ADMIN")
                    // Timesheet endpoints
                    .requestMatchers(HttpMethod.POST, "/api/v1/timesheets").hasAnyRole("EMPLOYEE", "HR_MANAGER", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v1/timesheets/**").hasAnyRole("EMPLOYEE", "HR_MANAGER", "ADMIN")
                    .requestMatchers("/api/v1/timesheet-approvals/**").hasAnyRole("HR_MANAGER", "ADMIN")
                    // Payroll endpoints - accessible by HR_MANAGER, ADMIN
                    .requestMatchers("/api/v1/payslips/**").hasAnyRole("HR_MANAGER", "ADMIN", "EMPLOYEE")
                    .requestMatchers("/api/v1/compensation-calculator/**").hasAnyRole("HR_MANAGER", "ADMIN")
                    // Admin only
                    .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                    // All other requests require authentication
                    .anyRequest().authenticated()
            );

        // Add JWT filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:5173", "http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

