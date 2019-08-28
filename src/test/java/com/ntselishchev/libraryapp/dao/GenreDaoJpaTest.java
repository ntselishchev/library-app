package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class GenreDaoJpaTest {

    @Autowired
    protected GenreDao genreDao;

    @Autowired
    private TestEntityManager em;

    private static final String NEW_GENRE_TITLE = "genre 1";
    private static final String NEW_GENRE_TITLE_2 = "genre 2";

    @Test
    public void testFindAllWhenThereAreMoreThanOneGenreShouldReturnGenres() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        em.persist(genre);
        Genre genre2 = new Genre(NEW_GENRE_TITLE_2);
        em.persist(genre2);
        em.flush();

        List<Genre> genres = genreDao.findAll();
        assertEquals(2, genres.size());
    }

    @Test
    public void testFindAllWhenThereAreNoGenresShouldReturnEmptyList() {
        List<Genre> genres = genreDao.findAll();

        assertTrue(genres.isEmpty());
    }
}
