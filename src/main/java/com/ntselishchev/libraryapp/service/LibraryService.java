package com.ntselishchev.libraryapp.service;

import com.ntselishchev.libraryapp.dao.AuthorDao;
import com.ntselishchev.libraryapp.dao.BookDao;
import com.ntselishchev.libraryapp.dao.GenreDao;
import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    public void addBook(String title, String authorId, String genreId) {
        Optional<Author> author = authorDao.findById(authorId);
        Optional<Genre> genre = genreDao.findById(genreId);

        author.ifPresent(a ->
                genre.ifPresent(g -> {
                    Book newBook = new Book(title, a, g);
                    bookDao.save(newBook);
        }));
    }

    public void deleteBook(String id) {
        bookDao.deleteById(id);
    }

    public void updateBook(String id, String title, String authorId, String genreId) {
        Optional<Book> book = bookDao.findById(id);
        Optional<Author> author = authorDao.findById(authorId);
        Optional<Genre> genre = genreDao.findById(genreId);

        book.ifPresent(b ->
            author.ifPresent(a ->
                genre.ifPresent(g -> {
                    b.setAuthor(a);
                    b.setGenre(g);
                    b.setTitle(title);
                    bookDao.save(b);
                })
            )
        );
    }

    public List<Author> getAuthors() {
        return authorDao.findAll();
    }

    public List<Genre> getGenres() {
        return genreDao.findAll();
    }

    public List<Book> getBooks() {
        return bookDao.findAll();
    }

    public Book getBook(String id) {
        Optional<Book> book = bookDao.findById(id);
        return book.orElse(null);
    }
}
