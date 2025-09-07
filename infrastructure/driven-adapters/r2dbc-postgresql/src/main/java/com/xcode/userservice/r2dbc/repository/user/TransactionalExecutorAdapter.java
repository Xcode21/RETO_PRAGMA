package com.xcode.userservice.r2dbc.repository.user;

import com.xcode.userservice.model.user.gateways.TransactionalExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TransactionalExecutorAdapter implements TransactionalExecutor {
    private final TransactionalOperator txOperator;

    @Override
    public <T> Mono<T> executeInTransaction(Mono<T> publisher) {
        return publisher.as(txOperator::transactional);
    }
}
