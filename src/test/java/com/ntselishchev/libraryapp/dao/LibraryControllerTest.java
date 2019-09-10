package com.ntselishchev.libraryapp.dao;

import com.ntselishchev.libraryapp.controller.LibraryController;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.dto.BookDTO;
import com.ntselishchev.libraryapp.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LibraryController.class)
public class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryService libraryService;

    private final String BOOK_ID = "book id";
    private final String BOOK_TITLE = "book title";
    private final String AUTHOR_ID = "author id";
    private final String GENRE_ID = "genre id";

    @Test
    public void testHomeShouldRedirectToGetAll() throws Exception {
        mockMvc.perform(get("/")
                .contentType("application/json"))
                .andExpect(redirectedUrl("/books/get-all"))
                .andExpect(status().isFound());
    }

    @Test
    public void testCreateBookWhenIsPostRequestAndRequestHasBookShouldInvokeLibraryServiceAndReturnIsFoundStatusAndRedirectToGetAll() throws Exception {
        BookDTO book = new BookDTO();
        book.setAuthorId(AUTHOR_ID);
        book.setGenreId(GENRE_ID);
        book.setTitle(BOOK_TITLE);

        mockMvc.perform(post("/books/create")
                .param("authorId", book.getAuthorId())
                .param("genreId", book.getGenreId())
                .param("title", book.getTitle()))
                .andExpect(redirectedUrl("/books/get-all"))
                .andExpect(status().isFound());

        verify(libraryService, Mockito.times(1)).addBook(book);
    }

    @Test
    public void testBooksAddWhenIsGetRequestShouldOkStatus() throws Exception {
        mockMvc.perform(get("/books/add")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllWhenIsGetRequestShouldReturnOkStatus() throws Exception {
        mockMvc.perform(get("/books/get-all")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteBookWhenIsDeleteRequestAndRequestHasIdShouldInvokeLibraryServiceAndReturnIsFoundStatusAndRedirectToGetAll() throws Exception {
        mockMvc.perform(delete("/books/delete")
                .contentType("application/json")
                .param("id", BOOK_ID))
                .andExpect(redirectedUrl("/books/get-all"))
                .andExpect(status().isFound());

        verify(libraryService, Mockito.times(1)).deleteBook(BOOK_ID);
    }

    @Test
    public void testUpdateBookWhenIsPutRequestAndRequestHasBookShouldInvokeLibraryServiceAndReturnIsFoundStatusAndRedirectToGetAll() throws Exception {
        BookDTO book = new BookDTO();
        book.setId(BOOK_ID);
        book.setAuthorId(AUTHOR_ID);
        book.setGenreId(GENRE_ID);
        book.setTitle(BOOK_TITLE);

        mockMvc.perform(put("/books/update")
                .param("id", book.getId())
                .param("authorId", book.getAuthorId())
                .param("genreId", book.getGenreId())
                .param("title", book.getTitle()))
                .andExpect(redirectedUrl("/books/get-all"))
                .andExpect(status().isFound());

        verify(libraryService, Mockito.times(1)).updateBook(book);
    }

    @Test
    public void testBooksEditWhenIsGetRequestShouldOkStatus() throws Exception {
        Book book = new Book();
        book.setId(BOOK_ID);
        book.setTitle(BOOK_TITLE);

        Mockito.when(libraryService.getBook(BOOK_ID)).thenReturn(book);

        mockMvc.perform(get("/books/edit")
                .param("id", BOOK_ID))
                .andExpect(status().isOk());

        verify(libraryService, times(1)).getBook(BOOK_ID);
    }
}
