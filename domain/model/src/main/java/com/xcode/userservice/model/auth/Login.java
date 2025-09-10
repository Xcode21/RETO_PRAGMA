package com.xcode.userservice.model.auth;
import com.xcode.userservice.model.user.exception.MissingRequiredFieldException;
import lombok.*;
//import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Login {

    private final String email;
    private final String password;

    public static Login of(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new MissingRequiredFieldException("Email");
        }
        if (password == null || password.isBlank()) {
            throw new MissingRequiredFieldException("Password");
        }
        return new Login(email.trim(), password);
    }
}

