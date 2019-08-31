package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
public class AuthorDaoJpaTest {

    @Autowired
    protected AuthorDao authorDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String NEW_AUTHOR_NAME = "author 1";
    private static final String NEW_AUTHOR_NAME_2 = "author 2";

    @BeforeEach
    public void setUp() {
        mongoTemplate.dropCollection(Author.class);
    }

    @Test
    public void testFindAllWhenThereAreMoreThanOneAuthorShouldReturnAuthors() {
        Author author = new Author(NEW_AUTHOR_NAME);
        mongoTemplate.save(author);
        Author author2 = new Author(NEW_AUTHOR_NAME_2);
        mongoTemplate.save(author2);

        List<Author> authors = authorDao.findAll();
        assertEquals(2, authors.size());
    }

    @Test
    public void testFindAllWhenThereAreNoAuthorsShouldReturnEmptyList() {
        List<Author> authors = authorDao.findAll();

        assertTrue(authors.isEmpty());
    }
}
