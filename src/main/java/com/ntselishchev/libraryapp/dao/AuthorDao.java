package com.ntselishchev.libraryapp.dao;


import com.ntselishchev.libraryapp.domain.Author;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorDao extends MongoRepository<Author, String> {

}
