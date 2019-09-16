package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DataMongoTest
public class AuthorDaoJpaTest {

    @Autowired
    protected AuthorDao authorDao;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    private static final String NEW_AUTHOR_NAME = "author 1";
    private static final String NEW_AUTHOR_NAME_2 = "author 2";

    @BeforeEach
    public void setUp() {
        mongoTemplate.dropCollection(Author.class).block();
    }

    @Test
    public void testFindAllWhenThereAreMoreThanOneAuthorShouldReturnAuthors() {
        Author author = new Author(NEW_AUTHOR_NAME);
        mongoTemplate.save(author).block();
        Author author2 = new Author(NEW_AUTHOR_NAME_2);
        mongoTemplate.save(author2).block();

        Flux<Author> authors = authorDao.findAll();

        StepVerifier
                .create(authors)
                .expectNextCount(2L)
                .expectComplete()
                .verify();
    }

    @Test
    public void testFindAllWhenThereAreNoAuthorsShouldReturnEmptyList() {
        Flux<Author> authors = authorDao.findAll();

        StepVerifier
                .create(authors)
                .expectNextCount(0L)
                .expectComplete()
                .verify();
    }
}
