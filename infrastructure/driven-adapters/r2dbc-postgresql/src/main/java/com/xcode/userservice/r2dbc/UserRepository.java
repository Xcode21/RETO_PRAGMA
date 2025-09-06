package com.xcode.userservice.r2dbc;

import com.xcode.userservice.model.user.User;
import com.xcode.userservice.r2dbc.dto.UserWithRoleDto;
import com.xcode.userservice.r2dbc.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, UUID>, ReactiveQueryByExampleExecutor<UserEntity> {

    @Query("SELECT EXISTS (SELECT 1 FROM users WHERE email = :email AND documento = :document)")
    Mono<Boolean> existsByEmailAndDocument(String email, String document);

    @Query("SELECT u.id, u.nombres, u.apellidos, u.fecha_nacimiento, u.direccion, " +
            "u.telefono, u.email, u.documento, u.salario_base, u.fecha_creacion, r.nombre as rol_nombre " +
            "FROM users u INNER JOIN rol r ON u.id_rol = r.id " +
            "WHERE u.id = :id")
    Mono<UserWithRoleDto> findByIdWithRole(UUID idUser);


}
