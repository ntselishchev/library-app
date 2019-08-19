package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations jdbc;

    private static final String BOOK_ID = "id";
    private static final String AUTHOR_ID = "authorId";
    private static final String GENRE_ID = "genreId";
    private static final String BOOK_TITLE = "title";

    @Override
    public Book findOneByTitleAndAuthorIdAndGenreId(String title, long authorId, long genreId) {
        final Map<String, Object> params = new HashMap<>();
        params.put(BOOK_TITLE, title);
        params.put(AUTHOR_ID, authorId);
        params.put(GENRE_ID, genreId);
        try {
            return jdbc.queryForObject(
                    "select b.*, " +
                    "a.name as author_name, " +
                    "g.title as genre_title " +
                    "from books b " +
                    "left join authors a on a.id = b.author_id " +
                    "left join genres g on g.id = b.genre_id " +
                    "where b.title = :title " +
                    "and author_id = :authorId " +
                    "and genre_id = :genreId " +
                    "limit 1"
                    , params, new BookMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Book findOneById(long id) {
        final Map<String, Object> params = new HashMap<>();
        params.put(BOOK_ID, id);
        try {
            return jdbc.queryForObject(
                    "select b.*, " +
                    "a.name as author_name, " +
                    "g.title as genre_title " +
                    "from books b " +
                    "left join authors a on a.id = b.author_id " +
                    "left join genres g on g.id = b.genre_id " +
                    "where b.id = :id " +
                    "limit 1"
                    , params, new BookMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void deleteById(long id) {
        final Map<String, Object> params = new HashMap<>();
        params.put(BOOK_ID, id);
        jdbc.update("delete from books where id = :id", params);
    }

    @Override
    public void update(long id, String title, long authorId, long genreId) {
        final Map<String, Object> params = new HashMap<>();
        params.put(BOOK_ID, id);
        params.put(BOOK_TITLE, title);
        params.put(AUTHOR_ID, authorId);
        params.put(GENRE_ID, genreId);

        jdbc.update(
                "update books " +
                "set " +
                "title = :title, " +
                "author_id = :authorId, " +
                "genre_id = :genreId " +
                "where id = :id"
                , params);
    }

    @Override
    public void saveOne(String title, long authorId, long genreId) {
        final Map<String, Object> params = new HashMap<>();
        params.put(BOOK_TITLE, title);
        params.put(AUTHOR_ID, authorId);
        params.put(GENRE_ID, genreId);

        jdbc.update(
                "insert into books (title, author_id, genre_id) " +
                "values (:title, :authorId, :genreId)"
                , params);
    }

    @Override
    public List<Book> findAll() {
        try {
            return jdbc.query(
                    "select b.*, " +
                    "a.name as author_name, " +
                    "g.title as genre_title " +
                    "from books b " +
                    "left join authors a on a.id = b.author_id " +
                    "left join genres g on g.id = b.genre_id "
                    , new BookMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
