package com.ntselishchev.libraryapp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue
    private long id;
    @Column
    private String title;
    @OneToOne(targetEntity = Author.class)
    @JoinColumn(name = "author_id")
    private Author author;
    @OneToOne(targetEntity = Genre.class)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public boolean hasSameParams(String title, long authorId, long genreId) {
        return this.title.equals(title) && author.getId() == authorId && genre.getId() == genreId;
    }
}
