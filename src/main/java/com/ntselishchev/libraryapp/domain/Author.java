package com.ntselishchev.libraryapp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "authors")
public class Author {

    @Id
    private String id;
    @Field("name")
    private String name;

    public Author(String name) {
        this.name = name;
    }
}
