package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Genre;

import java.util.List;

public interface GenreDao {

    List<Genre> findAll();
}
