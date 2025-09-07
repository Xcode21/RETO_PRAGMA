package com.xcode.userservice.r2dbc.mapper;

import com.xcode.userservice.model.rol.Role;
import com.xcode.userservice.model.rol.RolType;
import com.xcode.userservice.r2dbc.entity.RoleEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RoleMapper {

    public Role entityToDomain(RoleEntity entity) {
        if (entity == null) {
            return null;
        }
        Role role= Role.builder()
                .idRol(entity.getId())
                .type(stringToRolType(entity.getNombre()))
                .description(entity.getDescripcion())
                .build();
        return role;
    }

    public RoleEntity domainToEntity(Role domain) {
        if (domain == null) {
            return null;
        }
        
        return RoleEntity.builder()
                .id(domain.getIdRol())
                .nombre(rolTypeToString(domain.getType()))
                .descripcion(domain.getDescription())
                .build();
    }


    private RolType stringToRolType(String roleName) {
        return RolType.valueOf(roleName.toUpperCase().trim());
    }

    private String rolTypeToString(RolType rolType) {
        return rolType.name();
    }
}