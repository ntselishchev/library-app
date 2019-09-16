package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DataMongoTest
public class GenreDaoJpaTest {

    @Autowired
    protected GenreDao genreDao;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    private static final String NEW_GENRE_TITLE = "genre 1";
    private static final String NEW_GENRE_TITLE_2 = "genre 2";


    @BeforeEach
    public void setUp() {
        mongoTemplate.dropCollection(Genre.class).block();
    }

    @Test
    public void testFindAllWhenThereAreMoreThanOneGenreShouldReturnGenres() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        mongoTemplate.save(genre).block();
        Genre genre2 = new Genre(NEW_GENRE_TITLE_2);
        mongoTemplate.save(genre2).block();

        Flux<Genre> genres = genreDao.findAll();

        StepVerifier
                .create(genres)
                .expectNextCount(2L)
                .expectComplete()
                .verify();
    }

    @Test
    public void testFindAllWhenThereAreNoGenresShouldReturnEmptyList() {
        Flux<Genre> genres = genreDao.findAll();

        StepVerifier
                .create(genres)
                .expectNextCount(0L)
                .expectComplete()
                .verify();
    }
}
