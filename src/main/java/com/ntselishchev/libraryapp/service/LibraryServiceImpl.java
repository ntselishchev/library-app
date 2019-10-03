package com.ntselishchev.libraryapp.service;

import com.ntselishchev.libraryapp.dao.AuthorDao;
import com.ntselishchev.libraryapp.dao.BookDao;
import com.ntselishchev.libraryapp.dao.GenreDao;
import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Genre;
import com.ntselishchev.libraryapp.dto.BookDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("libraryService")
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    public void addBook(BookDTO bookDto) {
        Optional<Author> author = authorDao.findById(bookDto.getAuthorId());
        Optional<Genre> genre = genreDao.findById(bookDto.getGenreId());

        author.ifPresent(a ->
                genre.ifPresent(g -> {
                    Book newBook = new Book(bookDto.getTitle(), a, g);
                    bookDao.save(newBook);
        }));
    }

    public void deleteBook(String id) {
        bookDao.deleteById(id);
    }

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

    @PreAuthorize("hasAuthority('USER')")
    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<Book> getBooks() {
        return bookDao.findAll();
    }
}
