package com.sosyalmedia.customerservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    private final FileUploadProperties fileUploadProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Config'den gelen path: C:\Users\Approxy\Desktop\s\sosyalMediaProject\kaydedilenResimler
        String uploadPath = fileUploadProperties.getDirectory();

        // Windows için ters slash'leri düz slash'e çevir
        // C:\Users\... → C:/Users/...
        String normalizedPath = uploadPath.replace("\\", "/");

        // Spring Resource Handler için file protocol ekle
        // C:/Users/... → file:///C:/Users/...
        String resourceLocation = "file:///" + normalizedPath + "/";

        log.info("📂 Static files serving from: {}", resourceLocation);
        log.info("📍 Access URL pattern: /uploads/**");

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourceLocation)
                .setCachePeriod(3600);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}