package com.xcode.userservice.api.mapper;

import com.xcode.userservice.api.dto.LoginRequest;
import com.xcode.userservice.api.dto.UserRequest;
import com.xcode.userservice.api.dto.UserResponse;
import com.xcode.userservice.api.dto.UserValidationResponse;
import com.xcode.userservice.model.auth.Login;
import com.xcode.userservice.model.rol.Role;
import com.xcode.userservice.model.user.User;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface LoginMapper {
    default Login requestToDomain(LoginRequest request) {
        if (request == null) {
            return null;
        }
        return Login.of(request.getEmail(), request.getPassword());
    }

    default UserResponse domainToResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .idUser(user.getIdUser().toString())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .phone(user.getPhone())
                .email(user.getEmail())
                .documento(user.getDocument())
                .baseSalary(user.getSalaryBase())
                .roleName(user.getRole() != null ? user.getRole().getType().name() : null)
                .build();
    }


    default UserValidationResponse buildSuccessResponse(String documento, Boolean isValid) {
        return UserValidationResponse.builder()
                .documento(documento)
                .active(isValid)
                .status(isValid ? "ACTIVE" : "INACTIVE")
                .message(isValid ? "Active and valid user" : "Inactive user")
                .validatedAt(LocalDateTime.now())
                .build();
    }
}
