package com.xcode.userservice.model.auth.gateways;


import com.xcode.userservice.model.auth.vo.SecurityUser;
import com.xcode.userservice.model.auth.vo.Token;
import reactor.core.publisher.Mono;

public interface TokenGateway {

    Token generateToken(SecurityUser user);

    Mono<Boolean> validateToken(String token);

    Mono<String> extractEmail(String token);
    
    Mono<String> extractRole(String token);

}
