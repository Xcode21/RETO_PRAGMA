package com.xcode.userservice.api;

import com.xcode.userservice.api.config.ValidationConfig;
import com.xcode.userservice.api.dto.ApiResponse;
import com.xcode.userservice.api.dto.UserRequest;
import com.xcode.userservice.api.dto.UserValidationResponse;
import com.xcode.userservice.api.dto.UserWebClientRequest;
import com.xcode.userservice.api.mapper.UserMapper;
import com.xcode.userservice.usecase.user.ExistByDocumentUserUseCase;
import com.xcode.userservice.usecase.user.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserHandler {
    private final ValidationConfig validationConfig;
    private final RegisterUserUseCase registerUserUseCase;
    private final ExistByDocumentUserUseCase existByDocumentUserUseCase;

    private final UserMapper userMapper;

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(UserRequest.class)
                .switchIfEmpty(Mono.error(new ServerWebInputException("Request body is required")))
                .flatMap(userRequest ->
                        Mono.fromCallable(() -> {
                            validationConfig.validate(userRequest);
                            return userRequest;
                        })
                )
                .map(userMapper::requestToDomain)
                .flatMap(registerUserUseCase::execute)
                .map(userMapper::domainToResponse)
                .flatMap(userResponse -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .bodyValue(ApiResponse.success("Usuario creado exitosamente", userResponse)));
    }

    public Mono<ServerResponse> validateUser(ServerRequest request) {
        String document = request.pathVariable("document");

        return Mono.fromCallable(() -> {
                    UserWebClientRequest dto = new UserWebClientRequest();
                    dto.setDocument(document);
                    validationConfig.validate(dto);
                    return dto.getDocument();
                })
                .flatMap(existByDocumentUserUseCase::execute)
             /*   .flatMap(isValid -> ServerResponse
                        .status(HttpStatus.OK)
                        .bodyValue(ApiResponse.success("Usuario validado exitosamente", userMapper.buildSuccessResponse(document, isValid))));
             */
                .flatMap(isValid ->buildSuccessResponse(document, isValid))
                .doOnError(error -> log.error("Error validating user with document: {}", document, error));
    }

    private Mono<ServerResponse> buildSuccessResponse(String documento, Boolean isValid) {
        UserValidationResponse response = userMapper.buildSuccessResponse(documento, isValid);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }
}
