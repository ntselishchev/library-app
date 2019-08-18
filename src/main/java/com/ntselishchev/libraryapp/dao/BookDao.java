package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Book;

import java.util.List;

public interface BookDao {

    Book findOneByTitleAndAuthorIdAndGenreId(String title, int authorId, int genreId);

    Book findOneById(int id);

    void deleteById(int id);

    void update(int id, String title, int authorId, int genreId);

    void saveOne(String title, int authorId, int genreId);

    List<Book> findAll();
}
