package com.ntselishchev.libraryapp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String content;
    @OneToOne(targetEntity = Book.class)
    @JoinColumn(name = "book_id")
    private Book book;

    public Comment(String content, Book book) {
        this.content = content;
        this.book = book;
    }
}
