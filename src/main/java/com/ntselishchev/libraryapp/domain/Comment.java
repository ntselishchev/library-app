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
@Document(value = "comments")
public class Comment {

    @Id
    private String id;
    @Field("content")
    private String content;
    @DBRef
    private Book book;

    public Comment(String content, Book book) {
        this.content = content;
        this.book = book;
    }
}
