package com.sosyalmedia.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // İzin verilen originler (Frontend URL'leri)
        corsConfig.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",  // React default
                "http://localhost:3001",  // Alternatif React port
                "http://localhost:4200"   // Angular default
        ));

        // İzin verilen HTTP metodları
        corsConfig.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        // İzin verilen headerlar
        corsConfig.setAllowedHeaders(List.of("*"));

        // Credentials desteği
        corsConfig.setAllowCredentials(true);

        // Max age
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}