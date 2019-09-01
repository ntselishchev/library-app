package com.ntselishchev.libraryapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.ntselishchev.libraryapp.dao")
public class LibraryAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryAppApplication.class, args);
	}

}
