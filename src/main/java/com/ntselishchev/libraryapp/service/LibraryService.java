package com.ntselishchev.libraryapp.service;

import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Genre;
import com.ntselishchev.libraryapp.dto.BookDTO;

import java.util.List;

public interface LibraryService {

    void addBook(BookDTO bookDto);

    void deleteBook(String id);

    void updateBook(String id, BookDTO bookDto);

    List<Author> getAuthors();

    List<Genre> getGenres();

    List<Book> getBooks();
}
