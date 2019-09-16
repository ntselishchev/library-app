package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    private final ReactiveMongoTemplate template;

    public Mono<Void> deleteBookWithRelatedCommentsByBookId(String id) {
        return template.remove(Query.query(Criteria.where("book").is(id)), Comment.class)
                .then(template.remove(Query.query(Criteria.where("id").is(id)), Book.class))
                .then();
    }

}
