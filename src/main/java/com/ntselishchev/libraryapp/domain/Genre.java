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
@Document(value = "genres")
public class Genre {

    @Id
    private String id;
    @Field("title")
    private String title;

    public Genre(String title) {
        this.title = title;
    }
}
