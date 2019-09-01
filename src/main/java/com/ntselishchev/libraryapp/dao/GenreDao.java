package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Genre;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GenreDao extends MongoRepository<Genre, String> {

}
