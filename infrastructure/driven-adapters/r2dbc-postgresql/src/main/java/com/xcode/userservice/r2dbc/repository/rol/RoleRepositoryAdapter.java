package com.xcode.userservice.r2dbc.repository.rol;

import com.xcode.userservice.model.rol.Role;
import com.xcode.userservice.r2dbc.entity.RoleEntity;
import com.xcode.userservice.r2dbc.exception.InfraErrorCode;
import com.xcode.userservice.r2dbc.exception.InfrastructureException;
import com.xcode.userservice.r2dbc.helper.ReactiveAdapterOperations;
import com.xcode.userservice.r2dbc.mapper.RoleMapper;
import lombok.extern.log4j.Log4j2;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
@Log4j2
public class RoleRepositoryAdapter extends ReactiveAdapterOperations<
        Role,
        RoleEntity,
        Integer,
        RoleRepository
        > implements com.xcode.userservice.model.rol.gateways.RoleRepository {
    private final RoleMapper mapper;

    public RoleRepositoryAdapter(RoleRepository repository, ObjectMapper mapper, RoleMapper roleMapper) {
        super(repository, mapper, d -> mapper.map(d, Role.class));
        this.mapper = roleMapper;
    }

    @Override
    public Mono<Role> findById(Integer idRol) {
        return repository.findById(idRol)
                .doOnSubscribe(sub -> log.info("Finding role: {}", idRol))
                .map(mapper::entityToDomain)
                .doOnNext(role -> log.info("Role found: {}", role.getType()))
                .doOnError(error -> log.error("Database error finding role: {}", idRol, error))
                .onErrorMap(ex -> new InfrastructureException(InfraErrorCode.DATABASE_ERROR, ex));
    }
}
