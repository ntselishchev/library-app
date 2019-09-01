package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Comment;
import com.ntselishchev.libraryapp.domain.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
public class CommentDaoJpaTest {

    @Autowired
    protected CommentDao commentDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String NEW_BOOK_TITLE = "book 1";

    private static final String NEW_COMMENT_CONTENT = "comment 1";
    private static final String NEW_COMMENT_CONTENT_2 = "comment 2";

    private static final String NEW_AUTHOR_NAME = "author 1";

    private static final String NEW_GENRE_TITLE = "genre 1";

    @BeforeEach
    public void setUp() {
        mongoTemplate.dropCollection(Comment.class);
        mongoTemplate.dropCollection(Genre.class);
        mongoTemplate.dropCollection(Author.class);
        mongoTemplate.dropCollection(Book.class);
    }

    @Test
    public void testFindAllByBookWhenThereAreMoreThanOneCommentShouldReturnComments() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        mongoTemplate.save(genre);
        Author author = new Author(NEW_AUTHOR_NAME);
        mongoTemplate.save(author);
        Book book = new Book(NEW_BOOK_TITLE, author, genre);
        mongoTemplate.save(book);
        Comment comment = new Comment(NEW_COMMENT_CONTENT, book);
        mongoTemplate.save(comment);
        Comment comment2 = new Comment(NEW_COMMENT_CONTENT_2, book);
        mongoTemplate.save(comment2);

        List<Comment> comments = commentDao.findAllByBook(book);
        assertEquals(2, comments.size());
    }

    @Test
    public void testFindAllByBookWhenThereAreNoCommentsShouldReturnEmptyList() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        mongoTemplate.save(genre);
        Author author = new Author(NEW_AUTHOR_NAME);
        mongoTemplate.save(author);
        Book book = new Book(NEW_BOOK_TITLE, author, genre);
        mongoTemplate.save(book);

        List<Comment> comments = commentDao.findAllByBook(book);

        assertTrue(comments.isEmpty());
    }

    @Test
    public void testSaveOneShouldSaveComment() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        mongoTemplate.save(genre);
        Author author = new Author(NEW_AUTHOR_NAME);
        mongoTemplate.save(author);
        Book book = new Book(NEW_BOOK_TITLE, author, genre);
        mongoTemplate.save(book);

        Comment comment = new Comment(NEW_COMMENT_CONTENT, book);
        Comment commentFound = commentDao.save(comment);

        assertEquals(comment.getContent(), commentFound.getContent());
        assertEquals(comment.getBook(), commentFound.getBook());
    }

    @Test
    public void testDeleteOneShouldDeleteComment() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        mongoTemplate.save(genre);
        Author author = new Author(NEW_AUTHOR_NAME);
        mongoTemplate.save(author);
        Book book = new Book(NEW_BOOK_TITLE, author, genre);
        mongoTemplate.save(book);
        Comment comment = new Comment(NEW_COMMENT_CONTENT, book);
        mongoTemplate.save(comment);

        commentDao.delete(comment);
        Comment commentFound = mongoTemplate.findById(comment.getId(), Comment.class);

        assertNull(commentFound);
    }
}
