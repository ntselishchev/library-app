package com.ntselishchev.libraryapp.controller;

import com.ntselishchev.libraryapp.dto.BookDTO;
import com.ntselishchev.libraryapp.service.LibraryServiceImpl;
import com.ntselishchev.libraryapp.service.LibraryUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LibraryController.class)
@WithMockUser(username="admin")
public class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryUserDetailsService libraryUserDetailsService;

    @MockBean
    private LibraryServiceImpl libraryService;

    private final String BOOK_ID = "book id";
    private final String BOOK_TITLE = "book title";
    private final String AUTHOR_ID = "author id";
    private final String GENRE_ID = "genre id";

    @Test
    public void testHomeShouldRedirectToGetAll() throws Exception {
        mockMvc.perform(get("/")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateBookWhenIsPostRequestAndRequestHasBookShouldInvokeLibraryServiceAndReturnIsFoundStatusAndRedirectToGetAll() throws Exception {
        BookDTO book = new BookDTO();
        book.setAuthorId(AUTHOR_ID);
        book.setGenreId(GENRE_ID);
        book.setTitle(BOOK_TITLE);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"authorId\": \"" + book.getAuthorId() + "\",\"title\": \"" + book.getTitle() + "\",\"genreId\" :\"" + book.getGenreId() + "\"}"))
                .andExpect(status().isOk());

        verify(libraryService, Mockito.times(1)).addBook(book);
    }

    @Test
    public void testGetAllWhenIsGetRequestShouldReturnOkStatus() throws Exception {
        mockMvc.perform(get("/books")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteBookWhenIsDeleteRequestAndRequestHasIdShouldInvokeLibraryServiceAndReturnIsFoundStatusAndRedirectToGetAll() throws Exception {
        mockMvc.perform(delete("/books/{id}", BOOK_ID)
                .contentType("application/json")
                .param("id", BOOK_ID))
                .andExpect(status().isOk());

        verify(libraryService, Mockito.times(1)).deleteBook(BOOK_ID);
    }

    @Test
    public void testUpdateBookWhenIsPutRequestAndRequestHasBookShouldInvokeLibraryServiceAndReturnIsFoundStatusAndRedirectToGetAll() throws Exception {
        BookDTO book = new BookDTO();
        book.setId(BOOK_ID);
        book.setAuthorId(AUTHOR_ID);
        book.setGenreId(GENRE_ID);
        book.setTitle(BOOK_TITLE);

        mockMvc.perform(put("/books/{id}", book.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"" + book.getId() +"\",\"authorId\": \"" + book.getAuthorId() + "\",\"title\": \"" + book.getTitle() + "\",\"genreId\" :\"" + book.getGenreId() + "\"}"))
                .andExpect(status().isOk());

        verify(libraryService, Mockito.times(1)).updateBook(book.getId(), book);
    }
}
