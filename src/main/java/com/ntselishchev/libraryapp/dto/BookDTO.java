package com.ntselishchev.libraryapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {

    private String id;
    private String title;
    private String authorId;
    private String genreId;
}
