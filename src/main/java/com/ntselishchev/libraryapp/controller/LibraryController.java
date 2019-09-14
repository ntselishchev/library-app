package com.ntselishchev.libraryapp.controller;

import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Genre;
import com.ntselishchev.libraryapp.dto.BookDTO;
import com.ntselishchev.libraryapp.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @RequestMapping("/")
    public String home(){
        return "redirect:/books/get-all";
    }

    @GetMapping("books/add")
    public String addBook(Model model) {
        List<Author> authorList = libraryService.getAuthors();
        List<Genre> genreList = libraryService.getGenres();
        model.addAttribute("authors", authorList);
        model.addAttribute("genres", genreList);
        return "add";
    }

    @PostMapping("books/create")
    public void createBook(@RequestBody BookDTO bookDto) {
        libraryService.addBook(bookDto);
    }

    @GetMapping("books/get-all")
    public List<Book> getBooks() {
        return libraryService.getBooks();
    }

    @DeleteMapping("books/delete")
    public void deleteBook(@RequestBody BookDTO bookDto) {
        libraryService.deleteBook(bookDto.getId());
    }

    @PutMapping("books/update")
    public void updateBook(@RequestBody BookDTO bookDto) {
        libraryService.updateBook(bookDto);
    }

    @GetMapping("books/get-genres")
    public List<Genre> getGenres() {
        return libraryService.getGenres();
    }

    @GetMapping("books/get-authors")
    public List<Author> getAuthors() {
        return libraryService.getAuthors();
    }
}
