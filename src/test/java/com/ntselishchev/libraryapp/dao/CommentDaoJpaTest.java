package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Comment;
import com.ntselishchev.libraryapp.domain.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(CommentDaoJpa.class)
public class CommentDaoJpaTest {

    @Autowired
    protected CommentDao commentDao;

    @Autowired
    private TestEntityManager em;

    private static final String NEW_BOOK_TITLE = "book 1";

    private static final String NEW_COMMENT_CONTENT = "comment 1";
    private static final String NEW_COMMENT_CONTENT_2 = "comment 2";

    private static final String NEW_AUTHOR_NAME = "author 1";

    private static final String NEW_GENRE_TITLE = "genre 1";

    @Test
    public void testFindAllByBookWhenThereAreMoreThanOneCommentShouldReturnComments() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        em.persist(genre);
        Author author = new Author(NEW_AUTHOR_NAME);
        em.persist(author);
        Book book = new Book(NEW_BOOK_TITLE, author, genre);
        em.persist(book);
        Comment comment = new Comment(NEW_COMMENT_CONTENT, book);
        em.persist(comment);
        Comment comment2 = new Comment(NEW_COMMENT_CONTENT_2, book);
        em.persist(comment2);
        em.flush();

        List<Comment> comments = commentDao.findAllByBook(book);
        assertEquals(2, comments.size());
    }

    @Test
    public void testFindAllByBookWhenThereAreNoCommentsShouldReturnEmptyList() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        em.persist(genre);
        Author author = new Author(NEW_AUTHOR_NAME);
        em.persist(author);
        Book book = new Book(NEW_BOOK_TITLE, author, genre);
        em.persist(book);
        em.flush();

        List<Comment> comments = commentDao.findAllByBook(book);

        assertTrue(comments.isEmpty());
    }

    @Test
    public void testSaveOneShouldSaveComment() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        em.persist(genre);
        Author author = new Author(NEW_AUTHOR_NAME);
        em.persist(author);
        Book book = new Book(NEW_BOOK_TITLE, author, genre);
        em.persist(book);
        em.flush();

        Comment comment = new Comment(NEW_COMMENT_CONTENT, book);
        Comment commentFound = commentDao.saveOne(comment);

        assertEquals(comment.getContent(), commentFound.getContent());
        assertEquals(comment.getBook(), commentFound.getBook());
    }

    @Test
    public void testDeleteOneShouldDeleteComment() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        em.persist(genre);
        Author author = new Author(NEW_AUTHOR_NAME);
        em.persist(author);
        Book book = new Book(NEW_BOOK_TITLE, author, genre);
        em.persist(book);
        Comment comment = new Comment(NEW_COMMENT_CONTENT, book);
        em.persist(comment);
        em.flush();

        commentDao.deleteOne(comment);
        Comment commentFound = em.find(Comment.class, comment.getId());

        assertNull(commentFound);
    }
}
