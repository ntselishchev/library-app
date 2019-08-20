package com.ntselishchev.libraryapp.controller;

import com.ntselishchev.libraryapp.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @ShellMethod(value = "add new book", key = {"ab", "add-book"})
    public void addBook() {
        libraryService.processAddBook();
    }

    @ShellMethod(value = "get all books", key = {"gb", "get-books"})
    public void getBooks() {
        libraryService.getBooks();
    }

    @ShellMethod(value = "delete book", key = {"db", "delete-book"})
    public void deleteBook() {
        libraryService.deleteBook();
    }

    @ShellMethod(value = "update book", key = {"ub", "update-book"})
    public void updateBook() {
        libraryService.updateBook();
    }
}
