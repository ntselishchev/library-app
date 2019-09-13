package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Comment;
import com.ntselishchev.libraryapp.domain.Genre;
import com.ntselishchev.libraryapp.events.MongoBookEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Import({MongoBookEventListener.class})
public class BookDaoJpaTest {

    @Autowired
    protected BookDao bookDao;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    private static final String FIRST_EXISTING_BOOK_TITLE = "title 1";
    private static final String SECOND_EXISTING_BOOK_TITLE = "title 2";
    private static final String NEW_BOOK_TITLE = "new title";
    private static final String NON_EXISTENT_BOOK_ID = "123";

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
    public void testFindOneByIdWhenRequestHasWrongIdShouldReturnNull() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        mongoTemplate.save(genre).block();
        Author author = new Author(NEW_AUTHOR_NAME);
        mongoTemplate.save(author).block();
        Book newBook = new Book(FIRST_EXISTING_BOOK_TITLE, author, genre);
        mongoTemplate.save(newBook).block();

        Mono<Book> book = bookDao.findById(NON_EXISTENT_BOOK_ID);

        StepVerifier
                .create(book)
                .expectNextCount(0L)
                .expectComplete()
                .verify();
    }

    @Test
    public void testFindOneByIdWhenRequestHasCorrectIdShouldReturnBook() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        mongoTemplate.save(genre).block();
        Author author = new Author(NEW_AUTHOR_NAME);
        mongoTemplate.save(author).block();
        Book newBook = new Book(FIRST_EXISTING_BOOK_TITLE, author, genre);
        mongoTemplate.save(newBook).block();

        Mono<Book> book = bookDao.findById(newBook.getId());

        StepVerifier
                .create(book)
                .assertNext(b -> {
                    assertEquals(newBook.getTitle(), b.getTitle());
                    assertEquals(author.getName(), b.getAuthor().getName());
                    assertEquals(genre.getTitle(), b.getGenre().getTitle());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void testFindAllWhenThereAreMoreThanOneBookShouldReturnBooks() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        mongoTemplate.save(genre).block();
        Author author = new Author(NEW_AUTHOR_NAME);
        mongoTemplate.save(author).block();
        Book newBook = new Book(FIRST_EXISTING_BOOK_TITLE, author, genre);
        mongoTemplate.save(newBook).block();
        Book newBook2 = new Book(SECOND_EXISTING_BOOK_TITLE, author, genre);
        mongoTemplate.save(newBook2).block();

        Flux<Book> books = bookDao.findAll();

        StepVerifier
                .create(books)
                .expectNextCount(2L)
                .expectComplete()
                .verify();
    }

    @Test
    public void testFindAllWhenThereAreNoBooksShouldReturnEmptyList() {
        Flux<Book> books = bookDao.findAll();

        StepVerifier
                .create(books)
                .expectNextCount(0L)
                .expectComplete()
                .verify();
    }

    @Test
    public void testSaveOneShouldSaveNewBook() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        mongoTemplate.save(genre).block();
        Author author = new Author(NEW_AUTHOR_NAME);
        mongoTemplate.save(author).block();

        Book savedBook = new Book(NEW_BOOK_TITLE, author, genre);
        bookDao.save(savedBook).block();

        Mono<Book> book = mongoTemplate.findById(savedBook.getId(), Book.class);

        StepVerifier
                .create(book)
                .assertNext(b -> {
                    assertEquals(NEW_BOOK_TITLE, b.getTitle());
                    assertEquals(author.getName(), b.getAuthor().getName());
                    assertEquals(genre.getTitle(), b.getGenre().getTitle());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void testDeleteByIdShouldDeleteBook() {
        Genre genre = new Genre(NEW_GENRE_TITLE);
        mongoTemplate.save(genre).block();
        Author author = new Author(NEW_AUTHOR_NAME);
        mongoTemplate.save(author).block();
        Book newBook = new Book(FIRST_EXISTING_BOOK_TITLE, author, genre);
        mongoTemplate.save(newBook).block();

        bookDao.deleteById(newBook.getId()).block();
        Mono<Book> book = mongoTemplate.findById(newBook.getId(), Book.class);

        StepVerifier
                .create(book)
                .expectNextCount(0L)
                .expectComplete()
                .verify();
    }


    //TODO doesnt work
//    @Test
//    public void testDeleteByIdWhenBookHasCommentsShouldDeleteBookAndRelatedComments() {
//        Genre genre = new Genre(NEW_GENRE_TITLE);
//        mongoTemplate.save(genre).block();
//        Author author = new Author(NEW_AUTHOR_NAME);
//        mongoTemplate.save(author).block();
//        Book newBook = new Book(FIRST_EXISTING_BOOK_TITLE, author, genre);
//        mongoTemplate.save(newBook).block();
//        Comment newComment = new Comment("test content", newBook);
//        mongoTemplate.save(newComment).block();
//
//        bookDao.deleteById(newBook.getId()).block();
//
//        Mono<Book> book = mongoTemplate.findById(newBook.getId(), Book.class);
//        Mono<Comment> comment = mongoTemplate.findById(newComment.getId(), Comment.class);
//
//        StepVerifier
//                .create(book)
//                .expectNextCount(0L)
//                .expectComplete()
//                .verify();
//
//
//        StepVerifier
//                .create(comment)
//                .expectNextCount(0L)
//                .expectComplete()
//                .verify();
//    }

}
