package com.exhibyt.UserManagment.Configration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Allow all endpoints (or change to "/api/**" if more restrictive)
                        .allowedOrigins(
                                "http://localhost:3000",     // React CRA
                                "http://localhost:5173",     // Vite
                                "http://127.0.0.1:3000",      // Alternative CRA
                                "http://127.0.0.1:5173"       // Alternative Vite
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization", "Content-Type")
                        .allowCredentials(true)
                        .maxAge(3600); // Cache preflight response for 1 hour
            }
        };
    }
}
