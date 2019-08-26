package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(AuthorDaoJpa.class)
public class AuthorDaoJpaTest {

    @Autowired
    protected AuthorDao authorDao;

    @Autowired
    private TestEntityManager em;

    private static final String NEW_AUTHOR_NAME = "author 1";
    private static final String NEW_AUTHOR_NAME_2 = "author 2";

    @Test
    public void testFindAllWhenThereAreMoreThanOneAuthorShouldReturnAuthors() {
        Author author = new Author(NEW_AUTHOR_NAME);
        em.persist(author);
        Author author2 = new Author(NEW_AUTHOR_NAME_2);
        em.persist(author2);
        em.flush();

        List<Author> authors = authorDao.findAll();
        assertEquals(2, authors.size());
    }

    @Test
    public void testFindAllWhenThereAreNoAuthorsShouldReturnEmptyList() {
        List<Author> authors = authorDao.findAll();

        assertTrue(authors.isEmpty());
    }
}
