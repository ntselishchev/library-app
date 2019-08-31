package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentDao extends MongoRepository<Comment, String> {

    List<Comment> findAllByBook(Book book);

    void deleteAllByBook(Book book);
}
