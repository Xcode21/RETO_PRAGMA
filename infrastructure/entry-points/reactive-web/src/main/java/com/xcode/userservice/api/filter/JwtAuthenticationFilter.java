package com.xcode.userservice.api.filter;

import com.xcode.userservice.model.auth.gateways.TokenGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements WebFilter {
    
    private final TokenGateway tokenGateway;
    
    private static final String BEARER_PREFIX = "Bearer ";
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String method = exchange.getRequest().getMethod().name();

        if ("OPTIONS".equals(method)) {
            return chain.filter(exchange);
        }
        
        return extractToken(exchange)
                .flatMap(this::validateAndCreateAuthentication)
                .flatMap(authentication -> chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication)))
                .switchIfEmpty(chain.filter(exchange));
    }

    private Mono<String> extractToken(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith(BEARER_PREFIX))
                .map(authHeader -> authHeader.substring(BEARER_PREFIX.length()));
    }
    
    private Mono<UsernamePasswordAuthenticationToken> validateAndCreateAuthentication(String token) {
        return tokenGateway.validateToken(token)
                .filter(isValid -> isValid)
                .flatMap(isValid -> 
                    Mono.zip(
                        tokenGateway.extractEmail(token),
                        tokenGateway.extractRole(token)
                    )
                    .map(tuple -> {
                        String email = tuple.getT1();
                        String role = tuple.getT2();
                        return new UsernamePasswordAuthenticationToken(
                                email, 
                                null, 
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                        );
                    })
                )
                .doOnSuccess(auth -> log.debug("JWT token validated successfully with roles: {}", auth.getAuthorities()))
                .doOnError(error -> log.error("JWT token validation failed", error))
                .onErrorResume(throwable -> Mono.empty());
    }
}