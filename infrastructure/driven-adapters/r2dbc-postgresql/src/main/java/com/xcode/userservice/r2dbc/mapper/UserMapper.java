package com.xcode.userservice.r2dbc.mapper;

import com.xcode.userservice.model.rol.RolType;
import com.xcode.userservice.model.rol.Role;
import com.xcode.userservice.model.user.User;
import com.xcode.userservice.r2dbc.dto.UserWithRoleDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User dtoToDomain(UserWithRoleDto dto) {
        if (dto == null) return null;

        Role rol = Role.builder()
                .type(RolType.valueOf(dto.rolNombre())).build();
        return User.fromRepository(
                dto.id(),
                dto.firstName(),
                dto.lastName(),
                dto.email(),
                dto.document(),
                dto.phone(),
                rol,
                dto.salaryBase(),
                dto.fechaCreacion()
        );
    }

}
