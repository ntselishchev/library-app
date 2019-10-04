package com.ntselishchev.libraryapp.controller;

import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Genre;
import com.ntselishchev.libraryapp.dto.BookDTO;
import com.ntselishchev.libraryapp.service.IntegrationGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.PollableChannel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LibraryController {

    private final IntegrationGateway gateway;
    private final PollableChannel outputChannel;

    @PostMapping("/books")
    public void createBook(@RequestBody BookDTO bookDto) {
        gateway.process(bookDto,"addBook");
    }

    @GetMapping({"/", "books"})
    public List<Book> getBooks() {
        gateway.process("empty","getBooks");
        return (List<Book>) outputChannel.receive().getPayload();
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable("id") String id) {
        gateway.process(id,"deleteBook");
    }

    @PutMapping("/books/{id}")
    public void updateBook(@PathVariable String id, @RequestBody BookDTO bookDTO) {
        bookDTO.setId(id);
        gateway.process(bookDTO,"updateBook");
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        gateway.process("empty","getGenres");
        return (List<Genre>) outputChannel.receive().getPayload();
    }

    @GetMapping("/authors")
    public List<Author> getAuthors() {
        gateway.process("empty","getAuthors");
        return (List<Author>) outputChannel.receive().getPayload();
    }
}
