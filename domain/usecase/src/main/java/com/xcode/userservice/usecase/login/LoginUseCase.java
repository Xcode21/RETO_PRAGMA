package com.xcode.userservice.usecase.login;

import com.xcode.userservice.model.auth.Login;
import com.xcode.userservice.model.auth.gateways.PasswordGateway;
import com.xcode.userservice.model.auth.gateways.TokenGateway;
import com.xcode.userservice.model.auth.vo.SecurityUser;
import com.xcode.userservice.model.auth.vo.Token;
import com.xcode.userservice.model.user.exception.InvalidCredentialException;
import com.xcode.userservice.model.user.exception.InvalidEmailException;
import com.xcode.userservice.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordGateway passwordGateway;
    private final TokenGateway tokenGateway;

    public Mono<Token> execute(Login login) {
        return userRepository.findByEmailWithRole(login.getEmail())
                .switchIfEmpty(Mono.error(new InvalidEmailException(login.getEmail())))
                .filter(user -> passwordGateway.matches(login.getPassword(), user.getPassword()))
                .switchIfEmpty(Mono.error(new InvalidCredentialException(login.getEmail())))
                .map(SecurityUser::fromUser)
                .map(tokenGateway::generateToken);
    }

}
