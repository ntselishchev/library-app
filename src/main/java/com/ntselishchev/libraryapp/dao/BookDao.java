package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Book;

import java.util.List;

public interface BookDao {

    Book findOneByTitleAndAuthorIdAndGenreId(String title, long authorId, long genreId);

    Book findOneById(long id);

    void deleteById(long id);

    void update(long id, String title, long authorId, long genreId);

    Book saveOne(String title, long authorId, long genreId);

    List<Book> findAll();
}
