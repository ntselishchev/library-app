package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDao extends JpaRepository<Book, Long> {

    Book findOneByTitleAndAuthorIdAndGenreId(String title, long authorId, long genreId);

}
