package com.xcode.userservice.model.auth.vo;

import com.xcode.userservice.model.user.User;
import com.xcode.userservice.model.user.exception.MissingRequiredFieldException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SecurityUser {
    String id;
    String email;
    String fullName;
    String role;
    String documento;

    public static SecurityUser fromUser(User user) {
        if (user == null || user.getRole() == null) {
            throw new MissingRequiredFieldException("User or role");
        }

        return SecurityUser.builder()
                .id(user.getIdUser().toString())
                .email(user.getEmail())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .role(user.getRole().getType().name())
                .documento(user.getDocument())
                .build();
    }
}

