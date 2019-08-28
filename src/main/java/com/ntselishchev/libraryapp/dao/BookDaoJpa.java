package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class BookDaoJpa implements BookDao {

    @PersistenceContext
    private EntityManager em;

    private static final String AUTHOR_ID = "authorId";
    private static final String GENRE_ID = "genreId";
    private static final String BOOK_TITLE = "title";

    @Override
    public Book findOneByTitleAndAuthorIdAndGenreId(String title, long authorId, long genreId) {
        try {
            TypedQuery<Book> query = em.createQuery(
                    "select b from Book b " +
                    "join fetch b.author a " +
                    "join fetch b.genre g " +
                    "where b.title = :title " +
                    "and a.id = :authorId " +
                    "and g.id = :genreId ", Book.class);

            query.setParameter(BOOK_TITLE, title);
            query.setParameter(AUTHOR_ID, authorId);
            query.setParameter(GENRE_ID, genreId);

            return query.getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Book findOneById(long id) {
        try {
            return em.find(Book.class, id);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void deleteById(long id) {
        Book book = em.find(Book.class, id);
        em.remove(book);
    }

    @Override
    public void update(long id, String title, long authorId, long genreId) {
        Book book = em.find(Book.class, id);
        Author author = em.find(Author.class, authorId);
        Genre genre = em.find(Genre.class, genreId);

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);

        em.merge(book);
    }

    @Override
    public Book saveOne(String title, long authorId, long genreId) {
        Book book = new Book();
        Author author = em.find(Author.class, authorId);
        Genre genre = em.find(Genre.class, genreId);

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);

        em.persist(book);
        return book;
    }

    @Override
    public List<Book> findAll() {
        try {
            TypedQuery<Book> query = em.createQuery(
                    "select b from Book b " +
                    "join fetch b.author a " +
                    "join fetch b.genre g ", Book.class);

            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
