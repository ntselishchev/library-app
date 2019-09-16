package com.ntselishchev.libraryapp.dao;

import reactor.core.publisher.Mono;

public interface BookRepository {

    Mono<Void> deleteBookWithRelatedCommentsByBookId(String id);

}
