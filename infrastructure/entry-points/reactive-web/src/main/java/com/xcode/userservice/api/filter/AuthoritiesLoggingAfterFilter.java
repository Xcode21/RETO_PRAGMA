package com.xcode.userservice.api.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
public class AuthoritiesLoggingAfterFilter implements WebFilter {


 @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange)
                .then(Mono.defer(() -> ReactiveSecurityContextHolder.getContext()
                        .map(SecurityContext::getAuthentication)
                        .doOnNext(authentication -> {
                            if (authentication != null && authentication.isAuthenticated()) {
                                log.debug("User: {} is successfully logged in and has the following authorities: {}",
                                        authentication.getPrincipal(),
                                        authentication.getAuthorities());
                            }
                        })
                        .then()));
    }

}
