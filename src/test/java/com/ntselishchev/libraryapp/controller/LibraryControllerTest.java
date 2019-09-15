package com.ntselishchev.libraryapp.controller;

import com.google.common.net.HttpHeaders;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.dto.BookDTO;
import com.ntselishchev.libraryapp.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = LibraryController.class)
public class LibraryControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private LibraryService libraryService;

    private final String BOOK_ID = "book id";
    private final String BOOK_TITLE = "book title";
    private final String AUTHOR_ID = "author id";
    private final String GENRE_ID = "genre id";

    @Test
    public void testHomeShouldRedirectToGetAll() {
        webClient.get().uri("/").exchange()
                .expectStatus().isSeeOther().expectHeader().valueEquals(HttpHeaders.LOCATION, "/books/get-all");
    }

    @Test
    public void testCreateBookWhenIsPostRequestAndRequestHasBookShouldInvokeLibraryServiceAndReturnIsFoundStatusAndRedirectToGetAll() throws Exception {
        BookDTO book = new BookDTO();
        book.setAuthorId(AUTHOR_ID);
        book.setGenreId(GENRE_ID);
        book.setTitle(BOOK_TITLE);

        Mockito.when(libraryService.addBook(book)).thenReturn(Mono.when());

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/books/create")
                        .queryParam("authorId", book.getAuthorId())
                        .queryParam("genreId", book.getGenreId())
                        .queryParam("title", book.getTitle())
                        .build())
                .exchange()
                .expectStatus().isSeeOther().expectHeader().valueEquals(HttpHeaders.LOCATION, "/books/get-all");

        verify(libraryService, Mockito.times(1)).addBook(book);
    }

    @Test
    public void testBooksAddWhenIsGetRequestShouldOkStatus() {
        webClient.get().uri("/books/add").exchange().expectStatus().isOk();
    }

    @Test
    public void testGetAllWhenIsGetRequestShouldReturnOkStatus()  {
        webClient.get().uri("/books/get-all").exchange().expectStatus().isOk();
    }

    @Test
    public void testDeleteBookWhenIsDeleteRequestAndRequestHasIdShouldInvokeLibraryServiceAndReturnIsFoundStatusAndRedirectToGetAll() {
        Mockito.when(libraryService.deleteBook(BOOK_ID)).thenReturn(Mono.when());

        webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/books/delete")
                        .queryParam("id", BOOK_ID)
                        .build())
                .exchange()
                .expectStatus().isSeeOther().expectHeader().valueEquals(HttpHeaders.LOCATION, "/books/get-all");

        verify(libraryService, Mockito.times(1)).deleteBook(BOOK_ID);
    }

    @Test
    public void testUpdateBookWhenIsPutRequestAndRequestHasBookShouldInvokeLibraryServiceAndReturnIsFoundStatusAndRedirectToGetAll() {
        BookDTO book = new BookDTO();
        book.setId(BOOK_ID);
        book.setAuthorId(AUTHOR_ID);
        book.setGenreId(GENRE_ID);
        book.setTitle(BOOK_TITLE);

        Mockito.when(libraryService.updateBook(book)).thenReturn(Mono.when());

        webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/books/update")
                        .queryParam("id", book.getId())
                        .queryParam("authorId", book.getAuthorId())
                        .queryParam("genreId", book.getGenreId())
                        .queryParam("title", book.getTitle())
                        .build())
                .exchange()
                .expectStatus().isSeeOther().expectHeader().valueEquals(HttpHeaders.LOCATION, "/books/get-all");

        verify(libraryService, Mockito.times(1)).updateBook(book);
    }

    @Test
    public void testBooksEditWhenIsGetRequestShouldOkStatus() {
        Book book = new Book();
        book.setId(BOOK_ID);
        book.setTitle(BOOK_TITLE);

        Mockito.when(libraryService.getBook(BOOK_ID)).thenReturn(Mono.just(book));

        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/books/edit")
                        .queryParam("id", BOOK_ID)
                        .build())
                .exchange().expectStatus().isOk();

        verify(libraryService, times(1)).getBook(BOOK_ID);
    }
}