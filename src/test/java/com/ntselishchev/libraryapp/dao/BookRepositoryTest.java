package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Comment;
import com.ntselishchev.libraryapp.domain.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
@Import({BookRepositoryImpl.class})
public class BookRepositoryTest {

    @Autowired
    protected BookRepositoryImpl bookRepository;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    private static final String FIRST_EXISTING_BOOK_TITLE = "title 1";
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
    public void testDeleteByIdWhenBookHasCommentsShouldDeleteBookAndRelatedComments() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        mongoTemplate.save(genre).block();
        Author author = new Author(NEW_AUTHOR_NAME);
        mongoTemplate.save(author).block();
        Book newBook = new Book(FIRST_EXISTING_BOOK_TITLE, author, genre);
        mongoTemplate.save(newBook).block();
        Comment newComment = new Comment("test content", newBook);
        mongoTemplate.save(newComment).block();

        bookRepository.deleteById(newBook.getId()).block();

        Mono<Book> book = mongoTemplate.findById(newBook.getId(), Book.class);
        Mono<Comment> comment = mongoTemplate.findById(newComment.getId(), Comment.class);

        StepVerifier
                .create(book)
                .expectNextCount(0L)
                .expectComplete()
                .verify();

        StepVerifier
                .create(comment)
                .expectNextCount(0L)
                .expectComplete()
                .verify();
    }

}
