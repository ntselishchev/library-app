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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
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
    public String createBook(BookDTO bookDto, RedirectAttributes redirectAttributes) {
        libraryService.addBook(bookDto);
        redirectAttributes.addFlashAttribute("created", true);
        return "redirect:/books/get-all";
    }

    @GetMapping("books/get-all")
    public String getBooks(Model model) {
        List<Book> bookList = libraryService.getBooks();
        model.addAttribute("books", bookList);
        return "getAll";
    }

    @DeleteMapping("books/delete")
    public String deleteBook(String id, RedirectAttributes redirectAttributes) {
        libraryService.deleteBook(id);
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/books/get-all";
    }

    @PutMapping("books/update")
    public String updateBook(BookDTO bookDto, RedirectAttributes redirectAttributes) {
        libraryService.updateBook(bookDto);
        redirectAttributes.addFlashAttribute("updated", true);
        return "redirect:/books/get-all";
    }

    @GetMapping("books/edit")
    public String getBook(@RequestParam("id") String id, Model model) {
        Book book = libraryService.getBook(id);
        List<Author> authorList = libraryService.getAuthors();
        List<Genre> genreList = libraryService.getGenres();
        model.addAttribute("book", book);
        model.addAttribute("authors", authorList);
        model.addAttribute("genres", genreList);
        return "edit";
    }
}
