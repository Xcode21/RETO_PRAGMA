package com.xcode.userservice.api.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        ServerHttpResponse response = exchange.getResponse();
        
        if (!response.isCommitted()) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", "application/json");
            response.getHeaders().add("User-Error-Reason", "AUTHENTICATION_FAILED");
        }
        
        Map<String, Object> errorMap = createErrorMap(exchange, ex);
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorMap);
            return response.writeWith(
                    Mono.just(response.bufferFactory().wrap(bytes))
            );
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }

    private Map<String, Object> createErrorMap(ServerWebExchange exchange, AuthenticationException ex) {
        String errorMessage = "Unauthorized";

        if (ex instanceof BadCredentialsException) {
            errorMessage = ex.getMessage();
        }
        if (ex instanceof InsufficientAuthenticationException) {
            errorMessage = ex.getMessage();
        }
        if (ex instanceof CredentialsExpiredException) {
            errorMessage = ex.getMessage();
        }

        Map<String, Object> map = new HashMap<>(4);
        map.put("code", "SEC001");
        map.put("message", ex.getCause() != null ? ex.getCause().getMessage() : errorMessage);
        map.put("path", exchange.getRequest().getURI().getPath());
        //map.put("status", HttpStatus.UNAUTHORIZED.value());
        //map.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return map;
    }

}
