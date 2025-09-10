package com.xcode.userservice.api;


import com.xcode.userservice.api.config.ValidationConfig;
import com.xcode.userservice.api.dto.ApiResponse;
import com.xcode.userservice.api.dto.LoginRequest;
import com.xcode.userservice.api.mapper.LoginMapper;
import com.xcode.userservice.model.auth.gateways.TokenGateway;
import com.xcode.userservice.usecase.login.LoginUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
@Slf4j
public class AuthHandler {
    private final LoginMapper loginMapper;
    private final LoginUseCase loginUseCase;
    private final ValidationConfig validationConfig;
    private final TokenGateway tokenGateway;


    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .switchIfEmpty(Mono.error(new ServerWebInputException("Request body is required")))
                .doOnNext(validationConfig::validate)
                .map(loginMapper::requestToDomain)
                .flatMap(loginUseCase::execute)
                .flatMap(tokenResponse -> ServerResponse
                        .status(HttpStatus.OK)
                        .bodyValue(ApiResponse.success("Token generado exitosamente", tokenResponse)));


    }


}
