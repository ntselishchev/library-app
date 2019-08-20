package com.ntselishchev.libraryapp.mapper;

import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Genre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        final long id = resultSet.getLong("id");
        final String title = resultSet.getString("title");

        Author author = new Author();
        author.setId(resultSet.getLong("author_id"));
        author.setName(resultSet.getString("author_name"));

        Genre genre = new Genre();
        genre.setId(resultSet.getLong("genre_id"));
        genre.setTitle(resultSet.getString("genre_title"));

        return new Book(id, title, author, genre);
    }
}
