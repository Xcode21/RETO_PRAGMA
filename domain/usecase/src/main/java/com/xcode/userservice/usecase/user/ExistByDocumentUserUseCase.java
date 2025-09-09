package com.xcode.userservice.usecase.user;

import com.xcode.userservice.model.user.exception.UserNotFoundException;
import com.xcode.userservice.model.user.gateways.UserRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class ExistByDocumentUserUseCase {
    private final UserRepository userRepository;

    public Mono<Boolean> execute(String document) {
        return checkUserExistence(document.trim());
    }


    private Mono<Boolean> checkUserExistence(String document) {
        return userRepository.existsByDocument(document);
    }

}
