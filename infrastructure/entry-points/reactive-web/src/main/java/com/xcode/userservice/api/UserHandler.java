package com.xcode.userservice.api;

import com.xcode.userservice.api.config.ValidationConfig;
import com.xcode.userservice.api.dto.UserRequest;
import com.xcode.userservice.api.mapper.UserMapper;
import com.xcode.userservice.usecase.user.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class UserHandler {
    private final ValidationConfig validationConfig;
    private final RegisterUserUseCase  registerUserUseCase;
    private final UserMapper userMapper;
    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(UserRequest.class)
                .doOnNext(validationConfig::validate)
                .map(userMapper::requestToDomain)
                .flatMap(registerUserUseCase::execute)
                //.map(userDTOMapper::toResponse)
                .flatMap(user -> ServerResponse
                        .created(URI.create(request.uri() + "/" + user.getIdUser()))
                        .bodyValue(user));
    }
}
