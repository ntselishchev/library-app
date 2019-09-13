package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Comment;
import com.ntselishchev.libraryapp.domain.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DataMongoTest
public class CommentDaoJpaTest {

    @Autowired
    protected CommentDao commentDao;

    @Autowired
    protected BookDao bookDao;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    private static final String NEW_BOOK_TITLE = "book 1";

    private static final String NEW_COMMENT_CONTENT = "comment 1";

    private static final String NEW_AUTHOR_NAME = "author 1";

    private static final String NEW_GENRE_TITLE = "genre 1";

    @BeforeEach
    public void setUp() {
        mongoTemplate.dropCollection(Comment.class).block();
        mongoTemplate.dropCollection(Genre.class).block();
        mongoTemplate.dropCollection(Author.class).block();
        mongoTemplate.dropCollection(Book.class).block();
    }

    @Test
    public void testDeleteOneShouldDeleteComment() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        mongoTemplate.save(genre).block();
        Author author = new Author(NEW_AUTHOR_NAME);
        mongoTemplate.save(author).block();
        Book book = new Book(NEW_BOOK_TITLE, author, genre);
        String id = mongoTemplate.save(book).block().getId();
        Comment comment = new Comment(NEW_COMMENT_CONTENT, book);
        mongoTemplate.save(comment).block();

        mongoTemplate.remove(Query.query(Criteria.where("book").is(id)), Comment.class).block();

        Flux<Comment> comments = commentDao.findAll();

        StepVerifier
                .create(comments)
                .expectNextCount(0L)
                .expectComplete()
                .verify();
    }
}
