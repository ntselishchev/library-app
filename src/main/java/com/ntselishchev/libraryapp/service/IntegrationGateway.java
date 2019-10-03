package com.ntselishchev.libraryapp.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway
public interface IntegrationGateway {
    @Gateway(
            requestChannel = "bookFlow.input"
    )
    void process(Message msg);
}
