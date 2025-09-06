package com.xcode.userservice.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(UserHandler userHandler) {
        return route(GET("/api/usecase/path"), userHandler::listenGETUseCase)
                .andRoute(POST("/api/usecase/otherpath"), userHandler::listenPOSTUseCase)
                .and(route(GET("/api/otherusercase/path"), userHandler::listenGETOtherUseCase));
    }
}
