package com.xcode.userservice.model.user.gateways;

import com.xcode.userservice.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;

public interface UserRepository {
    Mono<User> save(User user);
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByDocument(String document);
    Mono<Boolean> existsByEmailAndDocument(String email, String document);
    Flux<User> findByEmailIn(Set<String> emails);
    Mono<User> findByDocument(String document);
    Mono<User> findByIdWithRole(UUID idUser);
    Mono<User> findByEmailWithRole(String email);
}
