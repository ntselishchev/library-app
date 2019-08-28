package com.ntselishchev.libraryapp.dao;


import com.ntselishchev.libraryapp.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorDao extends JpaRepository<Author, Long> {

}
