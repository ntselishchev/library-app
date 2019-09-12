package com.ntselishchev.libraryapp.dto;

import lombok.Data;

@Data
public class BookDTO {

    private String id;
    private String title;
    private String authorId;
    private String genreId;
}
