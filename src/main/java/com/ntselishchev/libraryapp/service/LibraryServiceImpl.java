package com.ntselishchev.libraryapp.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.ntselishchev.libraryapp.dao.AuthorDao;
import com.ntselishchev.libraryapp.dao.BookDao;
import com.ntselishchev.libraryapp.dao.GenreDao;
import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Genre;
import com.ntselishchev.libraryapp.dto.BookDTO;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service("libraryService")
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    private static final Logger availabilityLog = LoggerFactory.getLogger("availability-data");

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final MeterRegistry metric;

    @HystrixCommand(commandKey = "addBookKey", fallbackMethod = "buildAddBookFallback")
    public void addBook(BookDTO bookDto) {
        Optional<Author> author = authorDao.findById(bookDto.getAuthorId());
        Optional<Genre> genre = genreDao.findById(bookDto.getGenreId());

        author.ifPresent(a ->
                genre.ifPresent(g -> {
                    Book newBook = new Book(bookDto.getTitle(), a, g);
                    bookDao.save(newBook);
                    metric.counter("books.created").increment();
        }));
    }

    @HystrixCommand(commandKey = "deleteBookKey", fallbackMethod = "buildDeleteBookFallback")
    public void deleteBook(String id) {
        bookDao.deleteById(id);
        metric.counter("books.deleted").increment();
    }

    @HystrixCommand(commandKey = "updateBookKey", fallbackMethod = "buildUpdateBookFallback")
    public void updateBook(BookDTO bookDto) {
        Optional<Book> book = bookDao.findById(bookDto.getId());
        Optional<Author> author = authorDao.findById(bookDto.getAuthorId());
        Optional<Genre> genre = genreDao.findById(bookDto.getGenreId());

        book.ifPresent(b ->
            author.ifPresent(a ->
                genre.ifPresent(g -> {
                    b.setAuthor(a);
                    b.setGenre(g);
                    b.setTitle(bookDto.getTitle());
                    bookDao.save(b);
                    metric.counter("books.updated").increment();
                })
            )
        );
    }

    @HystrixCommand(commandKey = "getAuthorsKey", fallbackMethod = "buildGetAuthorsFallback")
    public List<Author> getAuthors() {
        return authorDao.findAll();
    }

    @HystrixCommand(commandKey = "getGenresKey", fallbackMethod = "buildGetGenresFallback")
    public List<Genre> getGenres() {
        return genreDao.findAll();
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostFilter("hasPermission(filterObject, 'READ')")
    @HystrixCommand(commandKey = "getBooksKey", fallbackMethod = "buildGetBooksFallback")
    public List<Book> getBooks() {
        return bookDao.findAll();
    }

    public List<Book> buildGetBooksFallback() {
        availabilityLog.error("unable to invoke DB in getBooks");
        return Collections.emptyList();
    }

    public List<Author> buildGetAuthorsFallback() {
        availabilityLog.error("unable to invoke DB in getAuthors");
        return Collections.emptyList();
    }

    public List<Genre> buildGetGenresFallback() {
        availabilityLog.error("unable to invoke DB in getGenres");
        return Collections.emptyList();
    }

    public void addBookFallback() {
        availabilityLog.error("unable to invoke DB in addBook");
    }

    public void deleteBookFallback() {
        availabilityLog.error("unable to invoke DB in deleteBook");
    }

    public void updateBookFallback() {
        availabilityLog.error("unable to invoke DB in updateBook");
    }
}
