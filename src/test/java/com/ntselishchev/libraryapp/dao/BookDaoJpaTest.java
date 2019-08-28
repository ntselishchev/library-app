package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BookDaoJpaTest {

    @Autowired
    protected BookDao bookDao;

    @Autowired
    private TestEntityManager em;

    private static final String FIRST_EXISTING_BOOK_TITLE = "title 1";
    private static final String SECOND_EXISTING_BOOK_TITLE = "title 2";
    private static final String WRONG_BOOK_TITLE = "other title";
    private static final String NEW_BOOK_TITLE = "new title";
    private static final long NON_EXISTENT_BOOK_ID = 7L;

    private static final long NON_EXISTENT_AUTHOR_ID = 7L;
    private static final String NEW_AUTHOR_NAME = "author 1";
    private static final String NEW_AUTHOR_NAME_2 = "author 2";

    private static final long NON_EXISTENT_GENRE_ID = 7L;
    private static final String NEW_GENRE_TITLE = "genre 1";
    private static final String NEW_GENRE_TITLE_2 = "genre 2";

    @Test
    public void testFindOneByTitleAndAuthorIdAndGenreIdWhenRequestHasWrongTitleShouldReturnNull() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        em.persist(genre);
        Author author = new Author(NEW_AUTHOR_NAME);
        em.persist(author);
        Book newBook = new Book(FIRST_EXISTING_BOOK_TITLE, author, genre);
        em.persist(newBook);
        em.flush();

        Book book = bookDao.findOneByTitleAndAuthorIdAndGenreId(WRONG_BOOK_TITLE, author.getId(), genre.getId());

        assertNull(book);
    }

    @Test
    public void testFindOneByTitleAndAuthorIdAndGenreIdWhenRequestHasWrongGenreIdShouldReturnNull() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        em.persist(genre);
        Author author = new Author(NEW_AUTHOR_NAME);
        em.persist(author);
        Book newBook = new Book(FIRST_EXISTING_BOOK_TITLE, author, genre);
        em.persist(newBook);
        em.flush();

        Book book = bookDao.findOneByTitleAndAuthorIdAndGenreId(newBook.getTitle(), author.getId(), NON_EXISTENT_GENRE_ID);

        assertNull(book);
    }

    @Test
    public void testFindOneByTitleAndAuthorIdAndGenreIdWhenRequestHasWrongAuthorIdShouldReturnNull() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        em.persist(genre);
        Author author = new Author(NEW_AUTHOR_NAME);
        em.persist(author);
        Book newBook = new Book(FIRST_EXISTING_BOOK_TITLE, author, genre);
        em.persist(newBook);
        em.flush();

        Book book = bookDao.findOneByTitleAndAuthorIdAndGenreId(newBook.getTitle(), NON_EXISTENT_AUTHOR_ID, genre.getId());

        assertNull(book);
    }

    @Test
    public void testFindOneByTitleAndAuthorIdAndGenreIdWhenRequestHasCorrectArgsShouldReturnBook() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        em.persist(genre);
        Author author = new Author(NEW_AUTHOR_NAME);
        em.persist(author);
        Book newBook = new Book(FIRST_EXISTING_BOOK_TITLE, author, genre);
        em.persist(newBook);
        em.flush();

        Book book = bookDao.findOneByTitleAndAuthorIdAndGenreId(newBook.getTitle(), author.getId(), genre.getId());

        assertEquals(newBook.getTitle(), book.getTitle());
        assertEquals(author.getName(), book.getAuthor().getName());
        assertEquals(genre.getTitle(), book.getGenre().getTitle());
    }

    @Test
    public void testFindOneByIdWhenRequestHasWrongIdShouldReturnNull() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        em.persist(genre);
        Author author = new Author(NEW_AUTHOR_NAME);
        em.persist(author);
        Book newBook = new Book(FIRST_EXISTING_BOOK_TITLE, author, genre);
        em.persist(newBook);
        em.flush();

        Optional<Book> book = bookDao.findById(NON_EXISTENT_BOOK_ID);

        assertFalse(book.isPresent());
    }

    @Test
    public void testFindOneByIdWhenRequestHasCorrectIdShouldReturnBook() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        em.persist(genre);
        Author author = new Author(NEW_AUTHOR_NAME);
        em.persist(author);
        Book newBook = new Book(FIRST_EXISTING_BOOK_TITLE, author, genre);
        em.persist(newBook);
        em.flush();

        Optional<Book> bookOpt = bookDao.findById(newBook.getId());
        Book book = bookOpt.orElse(null);

        assertNotNull(book);
        assertEquals(newBook.getTitle(), book.getTitle());
        assertEquals(author.getName(), book.getAuthor().getName());
        assertEquals(genre.getTitle(), book.getGenre().getTitle());
    }

    @Test
    public void testFindAllWhenThereAreMoreThanOneBookShouldReturnBooks() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        em.persist(genre);
        Author author = new Author(NEW_AUTHOR_NAME);
        em.persist(author);
        Book newBook = new Book(FIRST_EXISTING_BOOK_TITLE, author, genre);
        em.persist(newBook);
        Book newBook2 = new Book(SECOND_EXISTING_BOOK_TITLE, author, genre);
        em.persist(newBook2);
        em.flush();

        List<Book> books = bookDao.findAll();
        assertEquals(2, books.size());
    }

    @Test
    public void testFindAllWhenThereAreNoBooksShouldReturnEmptyList() {
        List<Book> books = bookDao.findAll();

        assertEquals(0, books.size());
    }

    @Test
    public void testUpdateShouldUpdateBookProps() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        em.persist(genre);
        Author author = new Author(NEW_AUTHOR_NAME);
        em.persist(author);
        Book newBook = new Book(FIRST_EXISTING_BOOK_TITLE, author, genre);
        em.persist(newBook);
        Genre newGenre = new Genre(NEW_GENRE_TITLE_2);
        em.persist(newGenre);
        Author newAuthor = new Author(NEW_AUTHOR_NAME_2);
        em.persist(newAuthor);
        em.flush();

        newBook.setTitle(NEW_BOOK_TITLE);
        newBook.setGenre(newGenre);
        newBook.setAuthor(newAuthor);

        bookDao.save(newBook);

        Book oldBook = bookDao.findOneByTitleAndAuthorIdAndGenreId(newBook.getTitle(), author.getId(), genre.getId());
        Book updatedBook = bookDao.findOneByTitleAndAuthorIdAndGenreId(NEW_BOOK_TITLE, newAuthor.getId(), newGenre.getId());

        assertNull(oldBook);
        assertEquals(NEW_BOOK_TITLE, updatedBook.getTitle());
        assertEquals(NEW_AUTHOR_NAME_2, updatedBook.getAuthor().getName());
        assertEquals(NEW_GENRE_TITLE_2, updatedBook.getGenre().getTitle());
    }

    @Test
    public void testSaveOneShouldSaveNewBook() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        em.persist(genre);
        Author author = new Author(NEW_AUTHOR_NAME);
        em.persist(author);
        em.flush();

        Book savedBook = new Book(NEW_BOOK_TITLE, author, genre);
        bookDao.save(savedBook);

        Book book = em.find(Book.class, savedBook.getId());

        assertEquals(NEW_BOOK_TITLE, book.getTitle());
        assertEquals(author.getName(), book.getAuthor().getName());
        assertEquals(genre.getTitle(), book.getGenre().getTitle());
    }

    @Test
    public void testDeleteByIdShouldDeleteBook() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        em.persist(genre);
        Author author = new Author(NEW_AUTHOR_NAME);
        em.persist(author);
        Book newBook = new Book(FIRST_EXISTING_BOOK_TITLE, author, genre);
        em.persist(newBook);
        em.flush();

        bookDao.deleteById(newBook.getId());
        Book book = em.find(Book.class, newBook.getId());

        assertNull(book);
    }

}
