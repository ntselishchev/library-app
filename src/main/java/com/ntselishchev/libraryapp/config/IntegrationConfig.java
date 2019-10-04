package com.ntselishchev.libraryapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;

@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

    @Bean
    public PollableChannel outputChannel() {
        return new QueueChannel(100);
    }

    @Bean
    public IntegrationFlow bookFlow() {
        return flow -> flow
                .route(Message.class, m -> m.getHeaders().get("operation", String.class),
                        mapping -> mapping
                                .subFlowMapping("getBooks", sub -> sub
                                        .handle("libraryService", "getBooks"))
                                .subFlowMapping("getAuthors", sub -> sub
                                        .handle("libraryService", "getAuthors"))
                                .subFlowMapping("getGenres", sub -> sub
                                        .handle("libraryService", "getGenres"))
                                .subFlowMapping("updateBook", sub -> sub
                                        .handle("libraryService", "updateBook"))
                                .subFlowMapping("addBook", sub -> sub
                                        .handle("libraryService", "addBook"))
                                .subFlowMapping("deleteBook", sub -> sub
                                        .handle("libraryService", "deleteBook")))
                .channel("outputChannel");
    }
}
