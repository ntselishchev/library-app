package com.ntselishchev.libraryapp.dao;


import com.ntselishchev.libraryapp.domain.Author;

import java.util.List;

public interface AuthorDao {

    List<Author> findAll();
}
