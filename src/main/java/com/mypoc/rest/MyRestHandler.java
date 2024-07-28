package com.mypoc.rest;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
class MyRestHandler {

    // <1>
    private final MyRestService myRestService;

    MyRestHandler(MyRestService myRestService) {
        this.myRestService = myRestService;
    }

    // <2>
    Mono<ServerResponse> getById(ServerRequest r) {
    	System.out.println(" In MyRestHandler getById: "  );
        return defaultReadResponse(this.myRestService.get(id(r)));
    }

    Mono<ServerResponse> all(ServerRequest r) {
    	System.out.println(" In MyRestHandler all: "  );
        return defaultReadResponse(this.myRestService.all());
    }

    Mono<ServerResponse> deleteById(ServerRequest r) {
    	System.out.println(" In MyRestHandler deleteById: "  );
        return defaultReadResponse(this.myRestService.delete(id(r)));
    }

    Mono<ServerResponse> updateById(ServerRequest r) {
    	System.out.println(" In MyRestHandler updateById: "  );
        Flux<MyClient> id = r.bodyToFlux(MyClient.class)
            .flatMap(p -> this.myRestService.update(p));//IMPORTANT tbd
        return defaultReadResponse(id);
    }

    Mono<ServerResponse> create(ServerRequest request) {
    	System.out.println(" In MyRestHandler create: "  );
        Flux<MyClient> flux = request
            .bodyToFlux(MyClient.class)
            .flatMap(toWrite -> this.myRestService.create(toWrite));//IMPORTANT tbd
        return defaultWriteResponse(flux);
    }

    // <3>
    private static Mono<ServerResponse> defaultWriteResponse(Publisher<MyClient> profiles) {
        return Mono
            .from(profiles)
            .flatMap(p -> ServerResponse
                .created(URI.create("/profiles/" + p.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        //        .contentType(MediaType.APPLICATION_JSON_UTF8) //Trying to Removing deprecated
                .build()
            );
    }

    // <4>
    private static Mono<ServerResponse> defaultReadResponse(Publisher<MyClient> profiles) {
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            //.contentType(MediaType.APPLICATION_JSON_UTF8) //Trying to Removing deprecated
            .body(profiles, MyClient.class);
    }

    private static Long id(ServerRequest r) {   // Change of return type of String to Long is my handiwork, let us see how it goes. This is for get and delete to work
        return Long.parseLong(r.pathVariable("id"));
    }
}
