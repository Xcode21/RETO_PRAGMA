package com.xcode.userservice.api.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorData {
    private List<ValidationFieldError> fieldErrors;
    private Integer totalErrors;
}
