package com.ntselishchev.libraryapp.service;

import com.ntselishchev.libraryapp.dao.AuthorDao;
import com.ntselishchev.libraryapp.dao.BookDao;
import com.ntselishchev.libraryapp.dao.GenreDao;
import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Genre;
import com.ntselishchev.libraryapp.dto.BookDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    public Mono<Void> addBook(BookDTO bookDto) {
        return authorDao.findById(bookDto.getAuthorId()).flatMap(a ->
                genreDao.findById(bookDto.getGenreId()).flatMap(g -> {
                    Book newBook = new Book(bookDto.getTitle(), a, g);
                    return bookDao.save(newBook);
                })
        ).then();
    }

    public Mono<Void> deleteBook(String id) {
        return bookDao.findById(id)
                .flatMap(b -> bookDao.deleteBookWithRelatedCommentsByBookId(id));
    }

    public Mono<Void> updateBook(BookDTO bookDto) {
        return bookDao.findById(bookDto.getId()).flatMap(b ->
                authorDao.findById(bookDto.getAuthorId()).flatMap(a ->
                        genreDao.findById(bookDto.getGenreId()).flatMap(g -> {
                            b.setAuthor(a);
                            b.setGenre(g);
                            b.setTitle(bookDto.getTitle());
                            return bookDao.save(b);
                        })
                )
        ).then();
    }

    public Flux<Author> getAuthors() {
        return authorDao.findAll();
    }

    public Flux<Genre> getGenres() {
        return genreDao.findAll();
    }

    public Flux<Book> getBooks() {
        return bookDao.findAll();
    }

    public Mono<Book> getBook(String id) {
        return bookDao.findById(id);
    }
}
