package com.xcode.userservice.model.rol.gateways;

import com.xcode.userservice.model.rol.Rol;
import reactor.core.publisher.Mono;

public interface RolRepository {
    Mono<Rol> getRolById(Integer id);
}
