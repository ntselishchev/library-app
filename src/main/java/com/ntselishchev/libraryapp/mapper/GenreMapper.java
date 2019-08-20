package com.ntselishchev.libraryapp.mapper;

import com.ntselishchev.libraryapp.domain.Genre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
        final long id = resultSet.getLong("id");
        final String title = resultSet.getString("title");
        return new Genre(id, title);
    }
}
