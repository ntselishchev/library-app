package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class CommentDaoJpa implements CommentDao {

    @PersistenceContext
    private EntityManager em;

    private static final String BOOK_ID = "bookId";

    @Override
    public List<Comment> findAll() {
        try {
            TypedQuery<Comment> query = em.createQuery("select a from Comment a", Comment.class);
            return query.getResultList();
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void saveOne(Comment comment) {
        em.persist(comment);
    }

    @Override
    public List<Comment> findAllByBook(Book book) {
        try {
            TypedQuery<Comment> query = em.createQuery(
                    "select a from Comment a " +
                    "join fetch a.book b " +
                    "where b.id = :bookId"
                    , Comment.class);
            query.setParameter(BOOK_ID, book.getId());
            return query.getResultList();
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void deleteOne(Comment comment) {
        em.remove(em.contains(comment) ? comment : em.merge(comment));
    }
}