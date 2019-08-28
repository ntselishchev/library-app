package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Comment;

import java.util.List;

public interface CommentDao {

    Comment saveOne(Comment comment);

    void deleteOne(Comment comment);

    List<Comment> findAllByBook(Book book);
}
