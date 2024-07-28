package com.mypoc.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
class MyEndpointConfiguration {

    @Bean  //IMPORTANT tbd
    RouterFunction<ServerResponse> routes(MyRestHandler handler) { // <1>
        return route(i(GET("/backsvc/clients3")), handler::all) // <2>
            .andRoute(i(GET("/backsvc/clients3/{id}")), handler::getById)
            .andRoute(i(DELETE("/backsvc/clients3/{id}")), handler::deleteById) // <3>
            .andRoute(i(POST("/backsvc/clients3")), handler::create)
            .andRoute(i(PUT("/backsvc/clients3/{id}")), handler::updateById);
    }

    // <4>
    private static RequestPredicate i(RequestPredicate target) {
        return new CaseAgnosticRequestPredicate(target);
    }
}