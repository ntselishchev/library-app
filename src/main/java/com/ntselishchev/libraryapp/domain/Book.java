package com.ntselishchev.libraryapp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Book {

    private long id;
    private String title;
    private Author author;
    private Genre genre;

    public boolean hasSameParams(String title, long authorId, long genreId) {
        return this.title.equals(title) && author.getId() == authorId && genre.getId() == genreId;
    }
}
