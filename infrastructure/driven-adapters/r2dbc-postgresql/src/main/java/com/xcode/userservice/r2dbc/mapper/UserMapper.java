package com.xcode.userservice.r2dbc.mapper;

import com.xcode.userservice.model.rol.RolType;
import com.xcode.userservice.model.rol.Role;
import com.xcode.userservice.model.user.User;
import com.xcode.userservice.r2dbc.dto.UserWithRoleDto;
import com.xcode.userservice.r2dbc.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User dtoToDomain(UserWithRoleDto dto) {
        if (dto == null) return null;

        Role rol = Role.builder()
                .idRol(dto.idRol())
                .type(RolType.valueOf(dto.roleName()))
                .description(dto.roleDescription()).build();
        return User.fromRepository(
                dto.id(),
                dto.firstName(),
                dto.lastName(),
                dto.birthDate(),
                dto.address(),
                dto.email(),
                dto.document(),
                dto.phone(),
                rol,
                dto.salaryBase(),
                dto.password(),
                dto.createAt()
        );
    }
    public User dtoToDomain(UserEntity dto) {
        if (dto == null) return null;

        Role rol = Role.builder()
                .idRol(dto.getRol()).build();
        return User.fromRepository(
                dto.getIdUser(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getBirthDate(),
                dto.getAddress(),
                dto.getEmail(),
                dto.getDocument(),
                dto.getPhone(),
                rol,
                dto.getSalaryBase(),
                dto.getPassword(),
                dto.getCreatedAt()
        );
    }

    public UserEntity toEntity(User dto) {
        if (dto == null) return null;

        Role rol = Role.builder()
                .idRol(dto.getRole().getIdRol()).build();
        return UserEntity.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .document(dto.getDocument())
                .salaryBase(dto.getSalaryBase())
                .rol(dto.getRole().getIdRol())
                .password(dto.getPassword())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

}
