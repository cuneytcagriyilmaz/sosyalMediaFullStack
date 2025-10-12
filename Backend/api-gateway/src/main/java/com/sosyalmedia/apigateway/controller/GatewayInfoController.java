package com.sosyalmedia.apigateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gateway")
@RequiredArgsConstructor
public class GatewayInfoController {

    private final RouteLocator routeLocator;

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getGatewayInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("service", "API Gateway");
        info.put("version", "1.0.0");
        info.put("timestamp", LocalDateTime.now());
        info.put("status", "UP");

        return ResponseEntity.ok(info);
    }

    @GetMapping("/routes")
    public Flux<Map<String, Object>> getRoutes() {
        return routeLocator.getRoutes()
                .map(route -> {
                    Map<String, Object> routeInfo = new HashMap<>();
                    routeInfo.put("id", route.getId());
                    routeInfo.put("uri", route.getUri().toString());
                    routeInfo.put("order", route.getOrder());
                    routeInfo.put("predicates", route.getPredicate().toString());
                    routeInfo.put("filters", route.getFilters().toString());
                    return routeInfo;
                });
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.ok(health);
    }
}