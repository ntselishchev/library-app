package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookDao extends MongoRepository<Book, String> {

    List<Book> findAll();

    Book findOneByTitleAndAuthorIdAndGenreId(String title, String authorId, String genreId);

}
