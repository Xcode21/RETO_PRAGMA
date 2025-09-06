package com.xcode.userservice.model.rol.gateways;

import com.xcode.userservice.model.rol.Role;
import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<Role> findById(Integer id);
}
