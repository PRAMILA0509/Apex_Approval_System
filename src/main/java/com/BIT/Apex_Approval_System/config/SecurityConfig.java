package com.BIT.Apex_Approval_System.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Configuration
public class SecurityConfig {

    // Provide a PasswordEncoder bean and use it when creating users
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // ✅ 1. Define in-memory users and roles
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {
        UserDetails faculty = User.withUsername("faculty1")
                .password(encoder.encode("pass123"))
                .roles("FACULTY")
                .build();

        UserDetails hod = User.withUsername("hod1")
                .password(encoder.encode("pass123"))
                .roles("FORWARDER")
                .build();

        UserDetails dean = User.withUsername("dean1")
                .password(encoder.encode("pass123"))
                .roles("RECOMMENDER")
                .build();

        UserDetails principal = User.withUsername("principal")
                .password(encoder.encode("pass123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(faculty, hod, dean, principal);
    }

    // ✅ 2. Configure endpoint access rules
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {}) // Enable CORS and let Spring pick up CorsConfigurationSource
                .csrf(csrf -> csrf.disable()) // Disable CSRF for REST APIs

                .authorizeHttpRequests(auth -> auth
                        // Allow preflight requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Role-based access control
                        .requestMatchers("/api/forms").hasAnyRole("FACULTY", "FORWARDER", "RECOMMENDER", "ADMIN")
                        .requestMatchers("/api/forms/*/forward").hasRole("FORWARDER")
                        .requestMatchers("/api/forms/*/recommend").hasRole("RECOMMENDER")
                        .requestMatchers("/api/forms/*/approve").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                // ✅ Modern syntax for enabling login & basic auth
                .formLogin(form -> form.defaultSuccessUrl("/", true))
                .httpBasic(basic -> {}); // no permitAll() needed here

        return http.build();
    }
}
