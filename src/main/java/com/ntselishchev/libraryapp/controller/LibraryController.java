package com.ntselishchev.libraryapp.controller;

import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Genre;
import com.ntselishchev.libraryapp.dto.BookDTO;
import com.ntselishchev.libraryapp.service.IntegrationGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LibraryController {

    private final IntegrationGateway gateway;
    private final PollableChannel outputChannel;

    @PostMapping("/books")
    public void createBook(@RequestBody BookDTO bookDto) {
        gateway.process(MessageBuilder.withPayload(bookDto).setHeader("operation","addBook").build());
    }

    @GetMapping({"/", "books"})
    public List<Book> getBooks() {
        gateway.process(MessageBuilder.withPayload("empty").setHeader("operation","getBooks").build());
        return (List<Book>) outputChannel.receive().getPayload();
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable("id") String id) {
        gateway.process(MessageBuilder.withPayload(id).setHeader("operation","deleteBook").build());
    }

    @PutMapping("/books/{id}")
    public void updateBook(@PathVariable String id, @RequestBody BookDTO bookDTO) {
        bookDTO.setId(id);
        gateway.process(MessageBuilder.withPayload(bookDTO).setHeader("operation","updateBook").build());
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        gateway.process(MessageBuilder.withPayload("empty").setHeader("operation","getGenres").build());
        return (List<Genre>) outputChannel.receive().getPayload();
    }

    @GetMapping("/authors")
    public List<Author> getAuthors() {
        gateway.process(MessageBuilder.withPayload("empty").setHeader("operation","getAuthors").build());
        return (List<Author>) outputChannel.receive().getPayload();
    }
}
