package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDao extends MongoRepository<User, String> {

    User findByUserName(String userName);

}
