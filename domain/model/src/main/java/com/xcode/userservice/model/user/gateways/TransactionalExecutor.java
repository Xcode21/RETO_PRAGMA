package com.xcode.userservice.model.user.gateways;

import reactor.core.publisher.Mono;

public interface TransactionalExecutor {
    <T> Mono<T> executeInTransaction(Mono<T> publisher);
}
