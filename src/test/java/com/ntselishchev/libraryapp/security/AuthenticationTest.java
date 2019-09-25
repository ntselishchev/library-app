package com.ntselishchev.libraryapp.security;

import com.ntselishchev.libraryapp.controller.LibraryController;
import com.ntselishchev.libraryapp.dto.BookDTO;
import com.ntselishchev.libraryapp.service.LibraryService;
import com.ntselishchev.libraryapp.service.LibraryUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LibraryController.class)
public class AuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryUserDetailsService libraryUserDetailsService;

    @MockBean
    LibraryService libraryService;

    private final String BOOK_ID = "book id";
    private final String BOOK_TITLE = "book title";
    private final String AUTHOR_ID = "author id";
    private final String GENRE_ID = "genre id";

    @Test
    @WithAnonymousUser
    public void testGetBooksWhenIsNotAuthenticatedShouldRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/books")
                .contentType("application/json"))
                .andExpect(status().isFound())
                .andReturn()
                .getResponse().getRedirectedUrl().contains("/login");
    }


    @Test
    @WithAnonymousUser
    public void testGetAuthorsWhenIsNotAuthenticatedShouldRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/authors")
                .contentType("application/json"))
                .andExpect(status().isFound())
                .andReturn()
                .getResponse().getRedirectedUrl().contains("/login");
    }


    @Test
    @WithAnonymousUser
    public void testGetGenresWhenIsNotAuthenticatedShouldRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/genres")
                .contentType("application/json"))
                .andExpect(status().isFound())
                .andReturn()
                .getResponse().getRedirectedUrl().contains("/login");
    }

    @Test
    @WithAnonymousUser
    public void testPostBooksWhenIsNotAuthenticatedShouldRedirectToLoginPage() throws Exception {
        BookDTO book = new BookDTO();
        book.setAuthorId(AUTHOR_ID);
        book.setGenreId(GENRE_ID);
        book.setTitle(BOOK_TITLE);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"authorId\": \"" + book.getAuthorId() + "\",\"title\": \"" + book.getTitle() + "\",\"genreId\" :\"" + book.getGenreId() + "\"}"))
                .andExpect(status().isFound())
                .andReturn()
                .getResponse().getRedirectedUrl().contains("/login");

        verify(libraryService, never()).addBook(book);
    }

    @Test
    @WithAnonymousUser
    public void testDeleteBooksWhenIsNotAuthenticatedShouldRedirectToLoginPage() throws Exception {
        mockMvc.perform(delete("/books/{id}", BOOK_ID)
                .contentType("application/json")
                .param("id", BOOK_ID))
                .andExpect(status().isFound())
                .andReturn()
                .getResponse().getRedirectedUrl().contains("/login");

        verify(libraryService, never()).deleteBook(BOOK_ID);
    }

    @Test
    @WithAnonymousUser
    public void testUpdateBooksWhenIsNotAuthenticatedShouldRedirectToLoginPage() throws Exception {
        BookDTO book = new BookDTO();
        book.setId(BOOK_ID);
        book.setAuthorId(AUTHOR_ID);
        book.setGenreId(GENRE_ID);
        book.setTitle(BOOK_TITLE);

        mockMvc.perform(put("/books/{id}", book.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"" + book.getId() +"\",\"authorId\": \"" + book.getAuthorId() + "\",\"title\": \"" + book.getTitle() + "\",\"genreId\" :\"" + book.getGenreId() + "\"}"))
                .andExpect(status().isFound())
                .andReturn()
                .getResponse().getRedirectedUrl().contains("/login");

        verify(libraryService, never()).updateBook(book.getId(), book);
    }
}
