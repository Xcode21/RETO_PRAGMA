package com.xcode.userservice.api.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorData {
    private List<ValidationFieldError> fieldErrors;
    private Integer totalErrors;
}
