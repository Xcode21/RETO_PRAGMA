package com.xcode.userservice.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Slf4j
public class FunctionalErrorHandler extends AbstractErrorWebExceptionHandler {

    public FunctionalErrorHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources,
                                  ApplicationContext applicationContext, ServerCodecConfigurer configurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageReaders(configurer.getReaders());
        this.setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderException);
    }

    private Mono<ServerResponse> renderException(ServerRequest request) {
        Map<String, Object> error = this.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        HttpStatus status = getHttpStatus(error);
        cleanupErrorAttributes(error);
        return ServerResponse.status(status).contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(error));
    }

    private HttpStatus getHttpStatus(Map<String, Object> errorAttributes) {
        Object statusCode = errorAttributes.get("status");

        if (statusCode instanceof Integer) {
            try {
                return HttpStatus.valueOf((Integer) statusCode);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid HTTP status code: {}, using INTERNAL_SERVER_ERROR", statusCode);
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        log.warn("No status code found in error attributes, using INTERNAL_SERVER_ERROR");
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private void cleanupErrorAttributes(Map<String, Object> errorAttributes) {
        errorAttributes.remove("timestamp");
        errorAttributes.remove("error");
        errorAttributes.remove("requestId");
        errorAttributes.remove("trace");
    }
}