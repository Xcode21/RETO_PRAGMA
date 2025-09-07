package com.xcode.userservice.r2dbc.repository.rol;

import com.xcode.userservice.model.rol.Role;
import com.xcode.userservice.r2dbc.entity.RoleEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RoleRepository extends ReactiveCrudRepository<RoleEntity, Integer>, ReactiveQueryByExampleExecutor<RoleEntity> {

    Mono<RoleEntity> findById(Integer id);

}
