package com.sosyalmedia.apigateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/customer-service")
    public ResponseEntity<Map<String, Object>> customerServiceFallback() {
        log.error("Customer Service is currently unavailable. Fallback triggered.");

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("error", "Service Unavailable");
        response.put("message", "Customer Service is temporarily unavailable. Please try again later.");
        response.put("service", "customer-service");

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response);
    }

    @GetMapping("/default")
    public ResponseEntity<Map<String, Object>> defaultFallback() {
        log.error("A service is currently unavailable. Default fallback triggered.");

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("error", "Service Unavailable");
        response.put("message", "The requested service is temporarily unavailable. Please try again later.");

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response);
    }
}