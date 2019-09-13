package com.ntselishchev.libraryapp.dao;


import com.ntselishchev.libraryapp.domain.Author;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AuthorDao extends ReactiveMongoRepository<Author, String> {

}
