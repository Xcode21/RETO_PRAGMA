package com.xcode.userservice.api.mapper;

import com.xcode.userservice.api.dto.UserRequest;
import com.xcode.userservice.model.rol.Role;
import com.xcode.userservice.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    default User requestToDomain(UserRequest request) {
        if (request == null) {
            return null;
        }

        return User.createNew(
                request.getFirstName(),
                request.getLastName(),
                request.getBirthDate(),
                request.getAddress(),
                request.getPhone(),
                request.getEmail(),
                request.getDocumento(),
                request.getBaseSalary(),
                Role.builder().idRol(request.getRol()).build()

        );
    }

}
