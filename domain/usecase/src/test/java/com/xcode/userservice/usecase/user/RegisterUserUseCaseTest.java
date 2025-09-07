package com.xcode.userservice.usecase.user;

import com.xcode.userservice.model.rol.Role;
import com.xcode.userservice.model.rol.RolType;
import com.xcode.userservice.model.rol.gateways.RoleRepository;
import com.xcode.userservice.model.user.User;
import com.xcode.userservice.model.user.exception.DomainErrorCode;
import com.xcode.userservice.model.user.exception.DomainException;
import com.xcode.userservice.model.user.gateways.TransactionalExecutor;
import com.xcode.userservice.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private TransactionalExecutor txExecutor;

    private RegisterUserUseCase registerUserUseCase;

    @BeforeEach
    void setUp() {
        registerUserUseCase = new RegisterUserUseCase(userRepository, roleRepository, txExecutor);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        // Given
        User inputUser = createValidUser();
        User savedUser = inputUser.toBuilder().idUser(UUID.randomUUID()).build();
        User userWithRole = savedUser.toBuilder()
                .role(Role.builder().idRol(1).type(RolType.ADMIN).build())
                .build();

        Role validRole = Role.builder().idRol(1).type(RolType.ADMIN).build();

        when(userRepository.existsByEmailAndDocument(anyString(), anyString()))
                .thenReturn(Mono.just(false));
        when(roleRepository.findById(1))
                .thenReturn(Mono.just(validRole));
        when(userRepository.save(any(User.class)))
                .thenReturn(Mono.just(savedUser));
        when(userRepository.findByIdWithRole(any(UUID.class)))
                .thenReturn(Mono.just(userWithRole));
        when(txExecutor.executeInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When & Then
        StepVerifier.create(registerUserUseCase.execute(inputUser))
                .expectNext(userWithRole)
                .verifyComplete();

        verify(userRepository).existsByEmailAndDocument(inputUser.getEmail(), inputUser.getDocument());
        verify(roleRepository).findById(1);
        verify(userRepository).save(inputUser);
        verify(userRepository).findByIdWithRole(savedUser.getIdUser());
        verify(txExecutor).executeInTransaction(any(Mono.class));
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        // Given
        User inputUser = createValidUser();

        when(userRepository.existsByEmailAndDocument(anyString(), anyString()))
                .thenReturn(Mono.just(true));
        when(roleRepository.findById(1))
                .thenReturn(Mono.just(Role.builder().idRol(1).type(RolType.ADMIN).build()));
        when(userRepository.save(any(User.class)))
                .thenReturn(Mono.just(inputUser.toBuilder().idUser(UUID.randomUUID()).build()));
        when(txExecutor.executeInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When & Then
        StepVerifier.create(registerUserUseCase.execute(inputUser))
                .expectErrorMatches(throwable -> 
                    throwable instanceof DomainException &&
                    ((DomainException) throwable).getErrorCode().equals(DomainErrorCode.USER_ALREADY_EXISTS.getCode())
                )
                .verify();

        verify(userRepository).existsByEmailAndDocument(inputUser.getEmail(), inputUser.getDocument());
    }

    @Test
    void shouldThrowExceptionWhenRoleNotFound() {
        // Given
        User inputUser = createValidUser();

        when(userRepository.existsByEmailAndDocument(anyString(), anyString()))
                .thenReturn(Mono.just(false));
        when(roleRepository.findById(1))
                .thenReturn(Mono.empty());
        when(userRepository.save(any(User.class)))
                .thenReturn(Mono.just(inputUser.toBuilder().idUser(UUID.randomUUID()).build()));
        when(txExecutor.executeInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When & Then
        StepVerifier.create(registerUserUseCase.execute(inputUser))
                .expectErrorMatches(throwable -> 
                    throwable instanceof DomainException &&
                    ((DomainException) throwable).getErrorCode().equals(DomainErrorCode.ROLE_NOT_FOUND.getCode())
                )
                .verify();

        verify(userRepository).existsByEmailAndDocument(inputUser.getEmail(), inputUser.getDocument());
        verify(roleRepository).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenRoleNotAllowed() {
        // Given
        User inputUser = createValidUser();
        Role invalidRole = Role.builder()
                .idRol(1)
                .type(RolType.CLIENTE) // Role no permitido
                .build();

        when(userRepository.existsByEmailAndDocument(anyString(), anyString()))
                .thenReturn(Mono.just(false));
        when(roleRepository.findById(1))
                .thenReturn(Mono.just(invalidRole));
        when(userRepository.save(any(User.class)))
                .thenReturn(Mono.just(inputUser.toBuilder().idUser(UUID.randomUUID()).build()));
        when(txExecutor.executeInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When & Then
        StepVerifier.create(registerUserUseCase.execute(inputUser))
                .expectErrorMatches(throwable -> 
                    throwable instanceof DomainException &&
                    ((DomainException) throwable).getErrorCode().equals(DomainErrorCode.ROLE_NOT_ALLOWED.getCode())
                )
                .verify();

        verify(userRepository).existsByEmailAndDocument(inputUser.getEmail(), inputUser.getDocument());
        verify(roleRepository).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundAfterSave() {
        // Given
        User inputUser = createValidUser();
        User savedUser = inputUser.toBuilder().idUser(UUID.randomUUID()).build();
        Role validRole = Role.builder().idRol(1).type(RolType.ADMIN).build();

        when(userRepository.existsByEmailAndDocument(anyString(), anyString()))
                .thenReturn(Mono.just(false));
        when(roleRepository.findById(1))
                .thenReturn(Mono.just(validRole));
        when(userRepository.save(any(User.class)))
                .thenReturn(Mono.just(savedUser));
        when(userRepository.findByIdWithRole(any(UUID.class)))
                .thenReturn(Mono.empty()); // Usuario no encontrado después de guardar
        when(txExecutor.executeInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When & Then
        StepVerifier.create(registerUserUseCase.execute(inputUser))
                .expectErrorMatches(throwable -> 
                    throwable instanceof DomainException &&
                    ((DomainException) throwable).getErrorCode().equals(DomainErrorCode.USER_NOT_FOUND.getCode())
                )
                .verify();

        verify(userRepository).existsByEmailAndDocument(inputUser.getEmail(), inputUser.getDocument());
        verify(roleRepository).findById(1);
        verify(userRepository).save(inputUser);
        verify(userRepository).findByIdWithRole(savedUser.getIdUser());
    }

    @Test
    void shouldHandleTransactionalExecutorCorrectly() {
        // Given
        User inputUser = createValidUser();
        User savedUser = inputUser.toBuilder().idUser(UUID.randomUUID()).build();
        User userWithRole = savedUser.toBuilder()
                .role(Role.builder().idRol(1).type(RolType.ADMIN).build())
                .build();
        Role validRole = Role.builder().idRol(1).type(RolType.ADMIN).build();

        when(userRepository.existsByEmailAndDocument(anyString(), anyString()))
                .thenReturn(Mono.just(false));
        when(roleRepository.findById(1))
                .thenReturn(Mono.just(validRole));
        when(userRepository.save(any(User.class)))
                .thenReturn(Mono.just(savedUser));
        when(userRepository.findByIdWithRole(any(UUID.class)))
                .thenReturn(Mono.just(userWithRole));
        when(txExecutor.executeInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When & Then
        StepVerifier.create(registerUserUseCase.execute(inputUser))
                .expectNext(userWithRole)
                .verifyComplete();

        verify(txExecutor).executeInTransaction(any(Mono.class));
    }

    private User createValidUser() {
        Role role = Role.builder().idRol(1).build();
        
        return User.createNew(
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "123 Main St",
                "+1234567890",
                "john.doe@example.com",
                "12345678",
                50000.0,
                role,
                "password123"
        );
    }
}