package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Genre;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface GenreDao extends ReactiveMongoRepository<Genre, String> {

}
