package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Genre;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;

@DataJpaTest
@Import(BookDaoJpa.class)
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

        Assert.assertNull(book);
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

        Assert.assertNull(book);
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

        Assert.assertNull(book);
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

        Assert.assertEquals(newBook.getTitle(), book.getTitle());
        Assert.assertEquals(author.getName(), book.getAuthor().getName());
        Assert.assertEquals(genre.getTitle(), book.getGenre().getTitle());
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

        Book book = bookDao.findOneById(NON_EXISTENT_BOOK_ID);

        Assert.assertNull(book);
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

        Book book = bookDao.findOneById(newBook.getId());

        Assert.assertEquals(newBook.getTitle(), book.getTitle());
        Assert.assertEquals(author.getName(), book.getAuthor().getName());
        Assert.assertEquals(genre.getTitle(), book.getGenre().getTitle());
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
        Assert.assertEquals(2, books.size());
    }

    @Test
    public void testFindAllWhenThereAreNoBooksShouldReturnEmptyList() {
        List<Book> books = bookDao.findAll();

        Assert.assertEquals(0, books.size());
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

        bookDao.update(newBook.getId(), NEW_BOOK_TITLE, newAuthor.getId(), newGenre.getId());

        Book oldBook = bookDao.findOneByTitleAndAuthorIdAndGenreId(newBook.getTitle(), author.getId(), genre.getId());
        Book updatedBook = bookDao.findOneByTitleAndAuthorIdAndGenreId(NEW_BOOK_TITLE, newAuthor.getId(), newGenre.getId());

        Assert.assertNull(oldBook);
        Assert.assertEquals(NEW_BOOK_TITLE, updatedBook.getTitle());
        Assert.assertEquals(NEW_AUTHOR_NAME_2, updatedBook.getAuthor().getName());
        Assert.assertEquals(NEW_GENRE_TITLE_2, updatedBook.getGenre().getTitle());
    }

    @Test
    public void testSaveOneShouldSaveNewBook() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        em.persist(genre);
        Author author = new Author(NEW_AUTHOR_NAME);
        em.persist(author);
        em.flush();

        Book savedBook = bookDao.saveOne(NEW_BOOK_TITLE, author.getId(), genre.getId());
        Book book = em.find(Book.class, savedBook.getId());

        Assert.assertEquals(NEW_BOOK_TITLE, book.getTitle());
        Assert.assertEquals(author.getName(), book.getAuthor().getName());
        Assert.assertEquals(genre.getTitle(), book.getGenre().getTitle());
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

        Assert.assertNull(book);
    }

}
