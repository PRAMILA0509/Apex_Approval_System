package com.BIT.Apex_Approval_System.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // ✅ 1. Define in-memory users and roles
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails faculty = User.withDefaultPasswordEncoder()
                .username("faculty1")
                .password("pass123")
                .roles("FACULTY")
                .build();

        UserDetails hod = User.withDefaultPasswordEncoder()
                .username("hod1")
                .password("pass123")
                .roles("FORWARDER")
                .build();

        UserDetails dean = User.withDefaultPasswordEncoder()
                .username("dean1")
                .password("pass123")
                .roles("RECOMMENDER")
                .build();

        UserDetails principal = User.withDefaultPasswordEncoder()
                .username("principal")
                .password("pass123")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(faculty, hod, dean, principal);
    }

    // ✅ 2. Configure endpoint access rules
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for REST APIs

                .authorizeHttpRequests(auth -> auth
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
