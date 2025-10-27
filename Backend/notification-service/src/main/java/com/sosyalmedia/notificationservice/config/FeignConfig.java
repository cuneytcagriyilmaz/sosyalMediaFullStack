package com.sosyalmedia.notificationservice.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class FeignConfig {

    /**
     * RestTemplate Bean (Calendarific API için)
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Feign Logger Level
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * Request Interceptor (Header ekleme, logging vb.)
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("X-Source-Service", "notification-service");
            log.debug("Feign Request: {} {}",
                    requestTemplate.method(),
                    requestTemplate.url());
        };
    }

    /**
     * Custom Error Decoder
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            log.error("Feign Error: Method={}, Status={}, Reason={}",
                    methodKey,
                    response.status(),
                    response.reason());

            return new RuntimeException(
                    String.format("Feign client error: %s - Status: %d",
                            methodKey,
                            response.status())
            );
        };
    }
}