package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
public class GenreDaoJpaTest {

    @Autowired
    protected GenreDao genreDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String NEW_GENRE_TITLE = "genre 1";
    private static final String NEW_GENRE_TITLE_2 = "genre 2";


    @BeforeEach
    public void setUp() {
        mongoTemplate.dropCollection(Genre.class);
    }

    @Test
    public void testFindAllWhenThereAreMoreThanOneGenreShouldReturnGenres() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        mongoTemplate.save(genre);
        Genre genre2 = new Genre(NEW_GENRE_TITLE_2);
        mongoTemplate.save(genre2);

        List<Genre> genres = genreDao.findAll();
        assertEquals(2, genres.size());
    }

    @Test
    public void testFindAllWhenThereAreNoGenresShouldReturnEmptyList() {
        List<Genre> genres = genreDao.findAll();

        assertTrue(genres.isEmpty());
    }
}
