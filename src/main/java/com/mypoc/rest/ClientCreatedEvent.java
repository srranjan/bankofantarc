package com.mypoc.rest;

import org.springframework.context.ApplicationEvent;

public class ClientCreatedEvent extends ApplicationEvent {

    public ClientCreatedEvent(MyClient source) {
        super(source);
    }
}
