package com.ntselishchev.libraryapp.controller;

import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Genre;
import com.ntselishchev.libraryapp.dto.BookDTO;
import com.ntselishchev.libraryapp.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @PostMapping("/books")
    public void createBook(@RequestBody BookDTO bookDto) {
        libraryService.addBook(bookDto);
    }

    @GetMapping({"/", "books"})
    public List<Book> getBooks() {
        return libraryService.getBooks();
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable("id") String id) {
        libraryService.deleteBook(id);
    }

    @PutMapping("/books/{id}")
    public void updateBook(@PathVariable String id, @RequestBody BookDTO bookDTO) {
        libraryService.updateBook(id, bookDTO);
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return libraryService.getGenres();
    }

    @GetMapping("/authors")
    public List<Author> getAuthors() {
        return libraryService.getAuthors();
    }
}
