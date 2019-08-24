package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.LibraryAppApplicationTests;
import com.ntselishchev.libraryapp.domain.Genre;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;

@DataJpaTest
@Import(GenreDaoJpa.class)
public class GenreDaoJpaTest extends LibraryAppApplicationTests {

    @Autowired
    protected GenreDaoJpa genreDao;

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
        Assert.assertEquals(2, genres.size());
    }

    @Test
    public void testFindAllWhenThereAreNoGenresShouldReturnEmptyList() {
        List<Genre> genres = genreDao.findAll();

        Assert.assertTrue(genres.isEmpty());
    }
}
