package com.xcode.userservice.usecase.user;

import com.xcode.userservice.model.user.User;
import com.xcode.userservice.model.user.exception.InvalidEmailException;
import com.xcode.userservice.model.user.exception.UserNotFoundException;
import com.xcode.userservice.model.user.gateways.UserRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@AllArgsConstructor
public class ExistByEmailUserUseCase {
    private final UserRepository userRepository;

    public Flux<User> execute(Set<String> email) {
        return userRepository.findByEmailIn(email);
    }


}
