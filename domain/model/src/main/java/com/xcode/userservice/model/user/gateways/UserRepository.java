package com.xcode.userservice.model.user.gateways;

import com.xcode.userservice.model.user.User;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository {
    Mono<User> saveUser(User user);
    Mono<Boolean> existEmail(String email);
    Mono<Boolean> existDocumento(String document);
    Mono<User> getUser(String email);
    Mono<User> getUserByDocument(String document);
    Mono<User> getUserWithRole(UUID idUser);
    Mono<User> getUserWithRoleEmail(String email);
}
