package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.LibraryAppApplicationTests;
import com.ntselishchev.libraryapp.domain.Book;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@JdbcTest
@Import(BookDaoJdbc.class)
@Sql({"/db/schema.sql", "/db/import_book_dao_test.sql"})
public class BookDaoJdbcTest extends LibraryAppApplicationTests {

    @Autowired
    protected BookDao bookDaoJdbc;

    @Test
    public void testFindOneByTitleAndAuthorIdAndGenreIdWhenRequestHasWrongTitleShouldReturnNull() {
        Book book = bookDaoJdbc.findOneByTitleAndAuthorIdAndGenreId("wrong title", 1, 2);
        Assert.assertNull(book);
    }

    @Test
    public void testFindOneByTitleAndAuthorIdAndGenreIdWhenRequestHasWrongGenreIdShouldReturnNull() {
        Book book = bookDaoJdbc.findOneByTitleAndAuthorIdAndGenreId("title 1", 1, 7);
        Assert.assertNull(book);
    }

    @Test
    public void testFindOneByTitleAndAuthorIdAndGenreIdWhenRequestHasWrongAuthorIdShouldReturnNull() {
        Book book = bookDaoJdbc.findOneByTitleAndAuthorIdAndGenreId("title 1", 7, 2);
        Assert.assertNull(book);
    }

    @Test
    public void testFindOneByTitleAndAuthorIdAndGenreIdWhenRequestHasCorrectArgsShouldReturnBook() {
        Book book = bookDaoJdbc.findOneByTitleAndAuthorIdAndGenreId("title 1", 1, 2);
        Assert.assertEquals("title 1", book.getTitle());
        Assert.assertEquals(1, book.getAuthor().getId());
        Assert.assertEquals(2, book.getGenre().getId());
    }

    @Test
    public void testFindOneByIdWhenRequestHasWrongIdShouldReturnNull() {
        Book book = bookDaoJdbc.findOneById(55);
        Assert.assertNull(book);
    }

    @Test
    public void testFindOneByIdWhenRequestHasCorrectIdShouldReturnBook() {
        Book book = bookDaoJdbc.findOneById(1);
        Assert.assertEquals("title 1", book.getTitle());
        Assert.assertEquals(1, book.getAuthor().getId());
        Assert.assertEquals(2, book.getGenre().getId());
    }

    @Test
    public void testFindAllWhenThereAreMoreThanOneBookShouldReturnBooks() {
        List<Book> books = bookDaoJdbc.findAll();
        Assert.assertEquals(2, books.size());
    }

    @Test
    public void testUpdateShouldUpdateBookProps() {
        bookDaoJdbc.update(1, "updated title", 2, 3);
        Book oldBook = bookDaoJdbc.findOneByTitleAndAuthorIdAndGenreId("title 1", 1, 2);
        Book updatedBook = bookDaoJdbc.findOneByTitleAndAuthorIdAndGenreId("updated title", 2, 3);
        Assert.assertNull(oldBook);
        Assert.assertEquals("updated title", updatedBook.getTitle());
        Assert.assertEquals(2, updatedBook.getAuthor().getId());
        Assert.assertEquals(3, updatedBook.getGenre().getId());
    }

    @Test
    public void testSaveOneShouldSaveNewBook() {
        bookDaoJdbc.saveOne("title 2", 1, 2);
        Book book = bookDaoJdbc.findOneByTitleAndAuthorIdAndGenreId("title 2", 1, 2);
        Assert.assertEquals("title 2", book.getTitle());
        Assert.assertEquals(1, book.getAuthor().getId());
        Assert.assertEquals(2, book.getGenre().getId());
    }

    @Test
    public void testDeleteByIdShouldDeleteBook() {
        bookDaoJdbc.deleteById(2);
        Book deletedBook = bookDaoJdbc.findOneById(2);
        Assert.assertNull(deletedBook);
    }

    @Test
    public void testFindAllWhenThereAreNoBooksShouldReturnEmptyList() {
        bookDaoJdbc.deleteById(1);
        bookDaoJdbc.deleteById(2);
        List<Book> books = bookDaoJdbc.findAll();
        Assert.assertEquals(0, books.size());
    }
}
