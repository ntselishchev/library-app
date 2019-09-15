package com.ntselishchev.libraryapp.dao;

import reactor.core.publisher.Mono;

public interface BookRepository {

    Mono<Void> deleteById(String id);

}
