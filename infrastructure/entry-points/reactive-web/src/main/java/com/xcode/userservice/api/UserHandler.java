package com.xcode.userservice.api;

import com.xcode.userservice.api.config.ValidationConfig;
import com.xcode.userservice.api.dto.ApiResponse;
import com.xcode.userservice.api.dto.UserRequest;
import com.xcode.userservice.api.mapper.UserMapper;
import com.xcode.userservice.usecase.user.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {
    private final ValidationConfig validationConfig;
    private final RegisterUserUseCase  registerUserUseCase;
    private final UserMapper userMapper;

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(UserRequest.class)
                .switchIfEmpty(Mono.error(new ServerWebInputException("Request body is required")))
                .flatMap(userRequest -> {
                    try {
                        validationConfig.validate(userRequest);
                        return Mono.just(userRequest);
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                })
                .map(userMapper::requestToDomain)
                .flatMap(registerUserUseCase::execute)
                .map(userMapper::domainToResponse)
                .flatMap(userResponse -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .bodyValue(ApiResponse.success("Usuario creado exitosamente", userResponse)));
    }
}
