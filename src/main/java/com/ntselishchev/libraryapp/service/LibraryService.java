package com.ntselishchev.libraryapp.service;

import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Genre;
import com.ntselishchev.libraryapp.dto.BookDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LibraryService {

    Mono<Void> addBook(BookDTO bookDto);

    Mono<Void> deleteBook(String id);

    Mono<Void> updateBook(BookDTO bookDto);

    Flux<Author> getAuthors();

    Flux<Genre> getGenres();

    Flux<Book> getBooks();

    Mono<Book> getBook(String id);
}
