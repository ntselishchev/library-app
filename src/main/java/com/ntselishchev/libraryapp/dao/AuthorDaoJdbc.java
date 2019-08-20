package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.mapper.AuthorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<Author> findAll() {
        try {
            return jdbc.query("select * from authors", new AuthorMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
