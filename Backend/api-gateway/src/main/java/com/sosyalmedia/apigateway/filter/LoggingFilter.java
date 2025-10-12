package com.sosyalmedia.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Request Logging
        log.info("==========================================================");
        log.info("Incoming Request:");
        log.info("Time: {}", LocalDateTime.now().format(FORMATTER));
        log.info("Method: {}", request.getMethod());
        log.info("Path: {}", request.getPath());
        log.info("URI: {}", request.getURI());
        log.info("Headers: {}", request.getHeaders());
        log.info("Query Params: {}", request.getQueryParams());

        // Response Logging
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            log.info("----------------------------------------------------------");
            log.info("Outgoing Response:");
            log.info("Status Code: {}", response.getStatusCode());
            log.info("Headers: {}", response.getHeaders());
            log.info("==========================================================");
        }));
    }

    @Override
    public int getOrder() {
        return -1; // En önce çalışsın
    }
}