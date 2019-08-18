package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Genre;
import com.ntselishchev.libraryapp.mapper.GenreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<Genre> findAll() {
        try {
            return jdbc.query("select * from genres", new GenreMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
