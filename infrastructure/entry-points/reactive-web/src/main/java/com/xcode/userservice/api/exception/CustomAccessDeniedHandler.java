package com.xcode.userservice.api.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return Mono.defer(() -> {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.FORBIDDEN);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("code", "SEC-403");
            errorBody.put("message", Optional.ofNullable(denied.getMessage()).orElse("Access is denied"));
            errorBody.put("path", exchange.getRequest().getURI().getPath());
            //errorBody.put("timestamp", LocalDateTime.now().toString());

            try {
                byte[] bytes = objectMapper.writeValueAsBytes(errorBody);
                DataBuffer buffer = response.bufferFactory().wrap(bytes);

                return response.writeWith(Mono.just(buffer))
                        .doOnError(err -> DataBufferUtils.release(buffer));

            } catch (JsonProcessingException e) {
                log.error("Error serializing access denied response", e);
                return Mono.error(e);
            }
        });
    }

    private Map<String, Object> createErrorMap(ServerWebExchange exchange, AccessDeniedException ex) {

        Map<String, Object> map = new HashMap<>(4);
        map.put("timestamp", LocalDateTime.now().toString());
        map.put("status", HttpStatus.FORBIDDEN.value());
        map.put("error", HttpStatus.FORBIDDEN.getReasonPhrase());
        map.put("message", Optional.ofNullable(ex.getMessage()).orElse("Access is denied"));
        map.put("path", exchange.getRequest().getURI().getPath());
        return map;
    }
}
