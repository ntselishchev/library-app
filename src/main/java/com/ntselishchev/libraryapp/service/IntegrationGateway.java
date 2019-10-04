package com.ntselishchev.libraryapp.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

@MessagingGateway
public interface IntegrationGateway {
    @Gateway(
            requestChannel = "bookFlow.input"
    )
    void process(@Payload Object payload, @Header(name = "operation") String header);
}
