package com.xcode.userservice.api.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationFieldError {
    private String field;
    private String message;
    private Object rejectedValue;
}
