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

    private static final String SELECT_MAIN_QUERY =
            "select b.*, " +
            "a.name as author_name, " +
            "g.title as genre_title " +
            "from books b " +
            "left join authors a on a.id = b.author_id " +
            "left join genres g on g.id = b.genre_id ";

    private static final String FIND_ONE_BY_TITLE_AND_IDS_QUERY =
            "where b.title = :title " +
            "and author_id = :authorId " +
            "and genre_id = :genreId " +
            "limit 1";

    private static final String FIND_ONE_BY_ID_QUERY =
            "where b.id = :id " +
            "limit 1";

    private static final String INSERT_QUERY =
            "insert into books (title, author_id, genre_id) " +
            "values (:title, :authorId, :genreId)";

    private static final String DELETE_QUERY =
            "delete from books where id = :id";

    private static final String UPDATE_QUERY =
            "update books " +
            "set " +
            "title = :title, " +
            "author_id = :authorId, " +
            "genre_id = :genreId " +
            "where id = :id";

    @Override
    public Book findOneByTitleAndAuthorIdAndGenreId(String title, int authorId, int genreId) {
        final Map<String, Object> params = new HashMap<>();
        params.put("title", title);
        params.put("authorId", authorId);
        params.put("genreId", genreId);
        try {
            return jdbc.queryForObject(SELECT_MAIN_QUERY + FIND_ONE_BY_TITLE_AND_IDS_QUERY, params, new BookMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Book findOneById(int id) {
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        try {
            return jdbc.queryForObject(SELECT_MAIN_QUERY + FIND_ONE_BY_ID_QUERY, params, new BookMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void deleteById(int id) {
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        jdbc.update(DELETE_QUERY, params);
    }

    @Override
    public void update(int id, String title, int authorId, int genreId) {
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("title", title);
        params.put("authorId", authorId);
        params.put("genreId", genreId);

        jdbc.update(UPDATE_QUERY, params);
    }

    @Override
    public void saveOne(String title, int authorId, int genreId) {
        final Map<String, Object> params = new HashMap<>();
        params.put("title", title);
        params.put("authorId", authorId);
        params.put("genreId", genreId);

        jdbc.update(INSERT_QUERY, params);
    }

    @Override
    public List<Book> findAll() {
        try {
            return jdbc.query(SELECT_MAIN_QUERY, new BookMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
