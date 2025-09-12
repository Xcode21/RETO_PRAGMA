package com.xcode.userservice.api.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcode.userservice.api.exception.CustomAccessDeniedHandler;
import com.xcode.userservice.api.exception.CustomAuthenticationEntryPoint;
import com.xcode.userservice.api.filter.AuthoritiesLoggingAfterFilter;
import com.xcode.userservice.api.filter.JwtAuthenticationFilter;
import com.xcode.userservice.model.auth.gateways.TokenGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsWebFilter;

@Configuration
/*@Profile("prod")*/
@EnableWebFluxSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenGateway tokenGateway;
    private final CorsWebFilter corsWebFilter;


    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ObjectMapper objectMapper) {

        return http
                /*.redirectToHttps(rth-> rth.httpsRedirectWhen(e->true))*/
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/v1/users/login").permitAll()
                        .pathMatchers("/api/v1/users/salary").permitAll()
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                        .pathMatchers("/api/v1/users").hasAnyRole("ADMIN", "ASESOR")
                        .pathMatchers("/api/v1/users/by-document/**").hasRole("CLIENTE")
                        .anyExchange().authenticated()
                )
                .addFilterBefore(corsWebFilter, SecurityWebFiltersOrder.CORS)
                .addFilterBefore(new JwtAuthenticationFilter(tokenGateway), SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAfter(new AuthoritiesLoggingAfterFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .exceptionHandling(hbc -> hbc
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper))
                        .accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper)))
                .build();
    }

}
