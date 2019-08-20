package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.LibraryAppApplicationTests;
import com.ntselishchev.libraryapp.domain.Book;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@JdbcTest
@Import(BookDaoJdbc.class)
public class BookDaoJdbcTest extends LibraryAppApplicationTests {

    @Autowired
    protected BookDao bookDaoJdbc;

    private static final String FIRST_EXISTING_BOOK_TITLE = "title 1";
    private static final String SECOND_EXISTING_BOOK_TITLE = "title 2";
    private static final String NEW_BOOK_TITLE = "other title";

    private static long FIRST_EXISTING_BOOK_ID = 1L;
    private static long SECOND_EXISTING_BOOK_ID = 2L;
    private static final long NON_EXISTENT_BOOK_ID = 7L;

    private static final long FIRST_EXISTING_AUTHOR_ID = 1L;
    private static final long SECOND_EXISTING_AUTHOR_ID = 2L;
    private static final long NON_EXISTENT_AUTHOR_ID = 7L;

    private static final long SECOND_EXISTING_GENRE_ID = 2L;
    private static final long THIRD_EXISTING_GENRE_ID = 3L;
    private static final long NON_EXISTENT_GENRE_ID = 7L;

    @Test
    public void testFindOneByTitleAndAuthorIdAndGenreIdWhenRequestHasWrongTitleShouldReturnNull() {
        Book book = bookDaoJdbc.findOneByTitleAndAuthorIdAndGenreId(NEW_BOOK_TITLE, FIRST_EXISTING_AUTHOR_ID, SECOND_EXISTING_GENRE_ID);
        Assert.assertNull(book);
    }

    @Test
    public void testFindOneByTitleAndAuthorIdAndGenreIdWhenRequestHasWrongGenreIdShouldReturnNull() {
        Book book = bookDaoJdbc.findOneByTitleAndAuthorIdAndGenreId(FIRST_EXISTING_BOOK_TITLE, FIRST_EXISTING_AUTHOR_ID, NON_EXISTENT_GENRE_ID);
        Assert.assertNull(book);
    }

    @Test
    public void testFindOneByTitleAndAuthorIdAndGenreIdWhenRequestHasWrongAuthorIdShouldReturnNull() {
        Book book = bookDaoJdbc.findOneByTitleAndAuthorIdAndGenreId(FIRST_EXISTING_BOOK_TITLE, NON_EXISTENT_AUTHOR_ID, SECOND_EXISTING_GENRE_ID);
        Assert.assertNull(book);
    }

    @Test
    public void testFindOneByTitleAndAuthorIdAndGenreIdWhenRequestHasCorrectArgsShouldReturnBook() {
        Book book = bookDaoJdbc.findOneByTitleAndAuthorIdAndGenreId(FIRST_EXISTING_BOOK_TITLE, FIRST_EXISTING_AUTHOR_ID, SECOND_EXISTING_GENRE_ID);
        Assert.assertEquals(FIRST_EXISTING_BOOK_TITLE, book.getTitle());
        Assert.assertEquals(FIRST_EXISTING_AUTHOR_ID, book.getAuthor().getId());
        Assert.assertEquals(SECOND_EXISTING_GENRE_ID, book.getGenre().getId());
    }

    @Test
    public void testFindOneByIdWhenRequestHasWrongIdShouldReturnNull() {
        Book book = bookDaoJdbc.findOneById(NON_EXISTENT_BOOK_ID);
        Assert.assertNull(book);
    }

    @Test
    public void testFindOneByIdWhenRequestHasCorrectIdShouldReturnBook() {
        Book book = bookDaoJdbc.findOneById(FIRST_EXISTING_BOOK_ID);
        Assert.assertEquals(FIRST_EXISTING_BOOK_TITLE, book.getTitle());
        Assert.assertEquals(FIRST_EXISTING_AUTHOR_ID, book.getAuthor().getId());
        Assert.assertEquals(SECOND_EXISTING_GENRE_ID, book.getGenre().getId());
    }

    @Test
    public void testFindAllWhenThereAreMoreThanOneBookShouldReturnBooks() {
        List<Book> books = bookDaoJdbc.findAll();
        Assert.assertEquals(2, books.size());
    }

    @Test
    public void testUpdateShouldUpdateBookProps() {
        bookDaoJdbc.update(FIRST_EXISTING_BOOK_ID, NEW_BOOK_TITLE, SECOND_EXISTING_AUTHOR_ID, THIRD_EXISTING_GENRE_ID);
        Book oldBook = bookDaoJdbc.findOneByTitleAndAuthorIdAndGenreId(FIRST_EXISTING_BOOK_TITLE, FIRST_EXISTING_AUTHOR_ID, SECOND_EXISTING_GENRE_ID);
        Book updatedBook = bookDaoJdbc.findOneByTitleAndAuthorIdAndGenreId(NEW_BOOK_TITLE, SECOND_EXISTING_AUTHOR_ID, THIRD_EXISTING_GENRE_ID);
        Assert.assertNull(oldBook);
        Assert.assertEquals(NEW_BOOK_TITLE, updatedBook.getTitle());
        Assert.assertEquals(SECOND_EXISTING_AUTHOR_ID, updatedBook.getAuthor().getId());
        Assert.assertEquals(THIRD_EXISTING_GENRE_ID, updatedBook.getGenre().getId());
    }

    @Test
    public void testSaveOneShouldSaveNewBook() {
        bookDaoJdbc.saveOne(SECOND_EXISTING_BOOK_TITLE, FIRST_EXISTING_AUTHOR_ID, SECOND_EXISTING_GENRE_ID);
        Book book = bookDaoJdbc.findOneByTitleAndAuthorIdAndGenreId(SECOND_EXISTING_BOOK_TITLE, FIRST_EXISTING_AUTHOR_ID, SECOND_EXISTING_GENRE_ID);
        Assert.assertEquals(SECOND_EXISTING_BOOK_TITLE, book.getTitle());
        Assert.assertEquals(FIRST_EXISTING_AUTHOR_ID, book.getAuthor().getId());
        Assert.assertEquals(SECOND_EXISTING_GENRE_ID, book.getGenre().getId());
    }

    @Test
    public void testDeleteByIdShouldDeleteBook() {
        bookDaoJdbc.deleteById(SECOND_EXISTING_BOOK_ID);
        Book deletedBook = bookDaoJdbc.findOneById(SECOND_EXISTING_BOOK_ID);
        Assert.assertNull(deletedBook);
    }

    @Test
    public void testFindAllWhenThereAreNoBooksShouldReturnEmptyList() {
        bookDaoJdbc.deleteById(FIRST_EXISTING_BOOK_ID);
        bookDaoJdbc.deleteById(SECOND_EXISTING_BOOK_ID);
        List<Book> books = bookDaoJdbc.findAll();
        Assert.assertEquals(0, books.size());
    }
}
