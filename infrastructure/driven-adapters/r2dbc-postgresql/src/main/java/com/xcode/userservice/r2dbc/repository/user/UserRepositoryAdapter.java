package com.xcode.userservice.r2dbc.repository.user;

import com.xcode.userservice.model.user.User;
import com.xcode.userservice.r2dbc.entity.UserEntity;
import com.xcode.userservice.r2dbc.exception.InfraErrorCode;
import com.xcode.userservice.r2dbc.exception.InfrastructureException;
import com.xcode.userservice.r2dbc.helper.ReactiveAdapterOperations;
import com.xcode.userservice.r2dbc.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@Log4j2
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        UUID,
        UserRepository
        > implements com.xcode.userservice.model.user.gateways.UserRepository {
    public UserRepositoryAdapter(UserRepository repository, ObjectMapper mapper, UserMapper userMapper, TransactionalOperator txOperator) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.mapper = userMapper;
        this.txOperator = txOperator;
    }

    private final UserMapper mapper;
    private final TransactionalOperator txOperator;

    @Override
    public Mono<Boolean> existsByEmailAndDocument(String email, String document) {
        return repository.existsByEmailAndDocument(email, document)
                .doOnSubscribe(sub -> log.info("Finding user with email={} and document={}", email, document))
                .doOnSuccess(result -> log.info("Exist User: {}", result))
                .doOnError(error -> log.error("Error verify exist user in BD", error))
                .onErrorMap(ex -> new InfrastructureException(InfraErrorCode.DATABASE_ERROR, ex));
    }

    @Override
    public Mono<User> findByIdWithRole(UUID idUser) {
        return repository.findByIdWithRole(idUser)
                .doOnSubscribe(sub -> log.info("Finding user with role: {}", idUser))
                .map(mapper:: dtoToDomain)
                .doOnNext(user -> log.info("User found: {} (Role: {})", user.getIdUser(), user.getRole().getType()))
                .doOnError(error -> log.error("Database error finding user with role. ID: {}, Error: {}", idUser, error.getClass().getSimpleName(), error))
                .onErrorMap(ex -> new InfrastructureException(InfraErrorCode.DATABASE_ERROR, ex));
    }

    @Override
    public Mono<User> save(User user) {
        return repository.save(mapper.toEntity(user))
                .doOnSubscribe(sub -> log.info("Saving user - Name: {},  Role: {}", user.getFirstName(), user.getRole().getIdRol()))
                .map(mapper::dtoToDomain)
                .doOnNext(savedUser -> log.info("User saved successfully - ID: {}, Name: {}", savedUser.getIdUser(), savedUser.getFirstName()))
                .doOnError(error -> log.error("Error saving user - Name: {}, Error: {}", user.getIdUser(), error.getClass().getSimpleName(), error));
    }

    @Override
    public Mono<Boolean> existsByDocument(String document) {
        return repository.existsByDocument(document)
                .doOnSubscribe(sub -> log.info("Finding user - document: {}", document))
                .doOnSuccess(result -> log.info("Exists? {} for document {}", result, document))
                .doOnError(error -> log.error("Error querying DB for document: {}", document, error))
                .onErrorMap(ex -> new InfrastructureException(InfraErrorCode.DATABASE_ERROR, ex));
    }

    @Override
    public Mono<User> findByEmailWithRole(String email) {
        return repository.userWithRoleEmail(email)
                .doOnSubscribe(sub -> log.info("Finding user with email: {}", email))
                .map(mapper::dtoToDomain)
                .doOnError(error -> log.error("Error querying DB for email: {}", email, error))
                .onErrorMap(ex -> new InfrastructureException(InfraErrorCode.DATABASE_ERROR, ex));
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return null;
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return null;
    }

    @Override
    public Mono<User> findByDocument(String document) {
        return null;
    }

}
