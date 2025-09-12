package com.xcode.userservice.api;

import com.xcode.userservice.api.config.ValidationConfig;
import com.xcode.userservice.api.dto.*;
import com.xcode.userservice.api.mapper.UserMapper;
import com.xcode.userservice.usecase.user.ExistByDocumentUserUseCase;
import com.xcode.userservice.usecase.user.ExistByEmailUserUseCase;
import com.xcode.userservice.usecase.user.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserHandler {
    private final ValidationConfig validationConfig;
    private final RegisterUserUseCase registerUserUseCase;
    private final ExistByDocumentUserUseCase existByDocumentUserUseCase;
    private final ExistByEmailUserUseCase existByEmailUserUseCase;
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
                .flatMap(isValid ->buildSuccessResponse(document, isValid))
                .doOnError(error -> log.error("Error validating user with document: {}", document, error));
    }

    private Mono<ServerResponse> buildSuccessResponse(String documento, Boolean isValid) {
        UserValidationResponse response = userMapper.buildSuccessResponse(documento, isValid);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }

//    public Mono<ServerResponse> findSalaryByEmail(ServerRequest request) {
//        return request.bodyToMono(UserEmailWebClientRequest.class)
//                .doOnNext(validationConfig::validate) // validación del DTO
//                .map(UserEmailWebClientRequest::getEmail)
//                .flatMap(existByEmailUserUseCase::execute)
//                .flatMap(userResponse -> {
//                    UserEmailWebClientResponse response =
//                            new UserEmailWebClientResponse(userResponse.getEmail(), userResponse.getSalaryBase());
//
//                    return ServerResponse.ok()
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .bodyValue(response);
//                });
//    }

    public Mono<ServerResponse> findSalaryByEmail(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<Set<String>>() {})
                .flatMapMany(existByEmailUserUseCase::execute)
                .map(user -> new UserEmailWebClientResponse(user.getEmail(), user.getSalaryBase()))
                .collectList()
                .flatMap(responses -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(responses)
                )
                .onErrorResume(error -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .bodyValue(Map.of("error", error.getMessage())));
    }
}
