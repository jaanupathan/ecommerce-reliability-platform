package com.reliability.ecommerce.config;

import com.reliability.ecommerce.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // Public APIs
                .requestMatchers("/api/health").permitAll()
                .requestMatchers("/api/auth/**").permitAll()

                // Product APIs
                .requestMatchers(HttpMethod.GET, "/api/products/**")
                    .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/products/**")
                    .hasRole("SELLER")

                // Inventory APIs
                .requestMatchers(HttpMethod.GET, "/api/inventory/**")
                    .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/inventory/**")
                    .hasRole("SELLER")

                // Order APIs
                .requestMatchers(HttpMethod.POST, "/api/orders/**")
                    .hasRole("CUSTOMER")
                .requestMatchers(HttpMethod.GET, "/api/orders/**")
                    .authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/orders/**")
                    .hasRole("CUSTOMER")

                // Seller APIs
                .requestMatchers("/api/sellers/**")
                    .hasRole("SELLER")

                // Shipment APIs
                .requestMatchers("/api/shipments/**")
                    .authenticated()

                // Complaint APIs
                    .requestMatchers(HttpMethod.POST, "/api/complaints")
                    .hasRole("CUSTOMER")

                .requestMatchers(HttpMethod.GET, "/api/complaints/{id}")
                    .authenticated()

                .requestMatchers(HttpMethod.GET, "/api/complaints/status/**")
                    .hasRole("ADMIN")

                .requestMatchers(HttpMethod.PUT, "/api/complaints/{id}/status")
                    .hasRole("ADMIN")

                // Role-specific APIs
                .requestMatchers("/api/customer/**")
                    .hasRole("CUSTOMER")
                .requestMatchers("/api/seller/**")
                    .hasRole("SELLER")
                .requestMatchers("/api/admin/**")
                    .hasRole("ADMIN")
                .requestMatchers("/api/delivery/**")
                    .hasRole("DELIVERY_AGENT")
                 
                    .requestMatchers("/api/admin/**")
                    .hasRole("ADMIN")  
                    
                 .requestMatchers("/api/seller-reliability/**")
                    .authenticated()   
                  

                // MUST BE LAST
                .anyRequest().authenticated()
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}