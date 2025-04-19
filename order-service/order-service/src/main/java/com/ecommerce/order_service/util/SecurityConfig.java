package com.ecommerce.order_service.util;
//package com.ecommerce.order_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF using the new recommended way
                .csrf(AbstractHttpConfigurer::disable)

                // Authorize requests
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/orders/**").permitAll()  // No auth needed
                        .anyRequest().authenticated()  // All other endpoints need auth
                );

        // Add your JWT filter chain here if using JWT (optional placeholder)

        return http.build();
    }
}
