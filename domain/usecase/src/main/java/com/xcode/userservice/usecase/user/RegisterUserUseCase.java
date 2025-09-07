package com.xcode.userservice.usecase.user;

import com.xcode.userservice.model.rol.Role;
import com.xcode.userservice.model.rol.gateways.RoleRepository;
import com.xcode.userservice.model.user.User;
import com.xcode.userservice.model.user.exception.DomainErrorCode;
import com.xcode.userservice.model.user.exception.DomainException;
import com.xcode.userservice.model.user.gateways.TransactionalExecutor;
import com.xcode.userservice.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class RegisterUserUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TransactionalExecutor txExecutor;

    public Mono<User> execute(User user) {
        Mono<User> userResult= checkUserUniqueness(user)
                .then(validateRoleExists(user.getRole().getIdRol()))
                .then(userRepository.save(user))
                .flatMap(savedUser -> userRepository.findByIdWithRole(savedUser.getIdUser())
                        .switchIfEmpty(Mono.error(new DomainException(DomainErrorCode.USER_NOT_FOUND))));
        return txExecutor.executeInTransaction(userResult);
    }

    private Mono<Void> checkUserUniqueness(User user) {
        return userRepository.existsByEmailAndDocument(user.getEmail(), user.getDocument())
                .filter(exists -> !exists)
                .switchIfEmpty(Mono.error(new DomainException(DomainErrorCode.USER_ALREADY_EXISTS)))
                .then();
    }

    private Mono<Void> validateRoleExists(Integer roleId) {
        return roleRepository.findById(roleId)
                .switchIfEmpty(Mono.error(new DomainException(DomainErrorCode.ROLE_NOT_FOUND)))
                .filter(role -> role.isAdmin() || role.isAsesor())
                .switchIfEmpty(Mono.error(new DomainException(DomainErrorCode.ROLE_NOT_ALLOWED)))
                .then();
    }
}
