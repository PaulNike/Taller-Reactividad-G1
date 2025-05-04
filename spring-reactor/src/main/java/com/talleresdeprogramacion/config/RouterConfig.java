package com.talleresdeprogramacion.config;

import com.talleresdeprogramacion.handler.DishHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {
    @Bean
    public RouterFunction<ServerResponse> dishRoutes(DishHandler handler){
        return route(GET("/v3/dishes"), handler::findAll)
                .andRoute(GET("/v3/dishes/{id}"), handler::findById)
                .andRoute(POST("/v3/dishes"), handler::save)
                .andRoute(PUT("/v3/dishes/{id}"), handler::update);
    }
}
