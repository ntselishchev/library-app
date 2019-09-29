package com.ntselishchev.libraryapp.security;

import com.ntselishchev.libraryapp.controller.LibraryController;
import com.ntselishchev.libraryapp.service.LibraryService;
import com.ntselishchev.libraryapp.service.LibraryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@Import({LibraryController.class, LibraryServiceImpl.class})
@ContextConfiguration
@DataMongoTest
public class AuthorizationTest {

    @Autowired
    LibraryController controller;

    @Autowired
    private LibraryService libraryService;

//    @Test
//    @WithMockUser(username = "wrong", roles = {"WRONG"})
//    public void testGetBooksWhenIsNotAuthenticatedShouldRedirectToLoginPage() throws Exception {
//        controller.getBooks();
//
//        verify(libraryService, never()).getBooks();
//    }
}
