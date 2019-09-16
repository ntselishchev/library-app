package com.ntselishchev.libraryapp.controller;

import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Genre;
import com.ntselishchev.libraryapp.dto.BookDTO;
import com.ntselishchev.libraryapp.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @RequestMapping("/")
    public Mono<String> home(){
        return Mono.just("redirect:/books/get-all");
    }

    @GetMapping("books/add")
    public String addBook(Model model) {
        Flux<Author> authorList = libraryService.getAuthors();
        Flux<Genre> genreList = libraryService.getGenres();
        model.addAttribute("authors", authorList);
        model.addAttribute("genres", genreList);
        return "add";
    }

    @PostMapping("books/create")
    public Mono<String> createBook(BookDTO bookDto) {
         return libraryService.addBook(bookDto)
                 .then(Mono.just("redirect:/books/get-all"));
    }

    @GetMapping("books/get-all")
    public String getBooks(Model model) {
        Flux<Book> bookList = libraryService.getBooks();
        model.addAttribute("books", bookList);
        return "book-list";
    }

    @DeleteMapping("books/delete")
    public Mono<String> deleteBook(BookDTO bookDto) {
        return libraryService.deleteBook(bookDto.getId())
                .then(Mono.just("redirect:/books/get-all"));
    }

    @PutMapping("books/update")
    public Mono<String> updateBook(BookDTO bookDto) {
        return libraryService.updateBook(bookDto)
                .then(Mono.just("redirect:/books/get-all"));
    }

    @GetMapping("books/edit")
    public String getBook(@RequestParam("id") String id, Model model) {
        Mono<Book> book = libraryService.getBook(id);
        Flux<Author> authorList = libraryService.getAuthors();
        Flux<Genre> genreList = libraryService.getGenres();
        model.addAttribute("book", book);
        model.addAttribute("authors", authorList);
        model.addAttribute("genres", genreList);
        return "edit";
    }
}
