package com.xcode.userservice.model.auth.vo;

import com.xcode.userservice.model.user.exception.MissingRequiredFieldException;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Token {

    private final String accessToken;
    private final String tokenType;
    private final Long expiresIn;

    public static Token of(String accessToken, String tokenType, Long expiresIn) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new MissingRequiredFieldException("AccessToken");
        }
        if (tokenType == null || tokenType.isBlank()) {
            throw new MissingRequiredFieldException("TokenType");
        }
        if (expiresIn == null || expiresIn <= 0) {
            throw new MissingRequiredFieldException("ExpiresIn");
        }

        return new Token(accessToken, tokenType, expiresIn);
    }
}