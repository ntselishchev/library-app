package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Comment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentDao extends ReactiveMongoRepository<Comment, String> {

    Flux<Comment> findAllByBook(Book book);

    Mono<Void> deleteAllByBook(Book book);
}
