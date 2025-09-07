package com.xcode.userservice.api.config;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@AllArgsConstructor
public class ValidationConfig {
    private final Validator validator;


    public <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(v -> v.getPropertyPath().toString() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));
            throw new ConstraintViolationException(errorMessage, violations);
        }
    }

    public <T> Map<String, String> validateWithDetails(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (violations.isEmpty()) {
            return Collections.emptyMap();
        }
        return violations.stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));
    }
}