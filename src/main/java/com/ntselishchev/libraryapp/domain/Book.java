package com.ntselishchev.libraryapp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "books")
public class Book {

    @Id
    private String id;
    @Field("title")
    private String title;
    @DBRef
    private Author author;
    @DBRef
    private Genre genre;

    public boolean hasSameParams(String title, String authorId, String genreId) {
        return this.title.equals(title) && author.getId().equals(authorId) && genre.getId().equals(genreId);
    }

    public Book(String title, Author author, Genre genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
    }
}
