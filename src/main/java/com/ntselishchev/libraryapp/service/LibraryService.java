package com.ntselishchev.libraryapp.service;

import com.ntselishchev.libraryapp.dao.AuthorDao;
import com.ntselishchev.libraryapp.dao.BookDao;
import com.ntselishchev.libraryapp.dao.GenreDao;
import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final InOutService inOutService;
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    private static final String BOOK_ALREADY_EXISTS_MSG = "Book having provided parameters already exists";
    private static final String BOOK_SAVED_MSG = "Book has been added";
    private static final String BOOK_DELETED_MSG = "Book has been deleted";
    private static final String BOOK_UPDATED_MSG = "Book has been updated";
    private static final String BOOK_DOES_NOT_EXIST_MSG = "Book having provided id does not exist";
    private static final String AUTHOR_OR_GENRE_DOES_NOT_EXIST_MSG = "Selected author-id or genre-id does not exist";
    private static final String NO_BOOKS_AVAILABLE_MSG = "There are no saved books yet";

    public void processAddBook() {
        inOutService.print("Type new book title");
        String title = inOutService.getUserInputMessage();

        inOutService.print("Select author-id from authors below:");
        List<Author> authorList = getAuthors();
        long authorId = inOutService.getUserIntInputMessage();

        inOutService.print("Select genre-id from authors below:");
        List<Genre> genreList = getGenres();
        long genreId = inOutService.getUserIntInputMessage();

        boolean authorIdInRange = authorList.stream().map(Author::getId).collect(Collectors.toList()).contains(authorId);
        boolean genreIdInRange = genreList.stream().map(Genre::getId).collect(Collectors.toList()).contains(genreId);

        if (!authorIdInRange || !genreIdInRange) {
            inOutService.print(AUTHOR_OR_GENRE_DOES_NOT_EXIST_MSG);
            return;
        }

        Book existingBook = bookDao.findOneByTitleAndAuthorIdAndGenreId(title, authorId, genreId);
        if (existingBook == null) {
            addBook(title, authorId, genreId);
        } else {
            inOutService.print(BOOK_ALREADY_EXISTS_MSG);
        }
    }

    private void addBook(String title, long authorId, long genreId) {
        bookDao.saveOne(title, authorId, genreId);
        inOutService.print(BOOK_SAVED_MSG);
    }

//    public void displayBook(int id) {
//        Book existingBook = bookDao.findOneById(id);
//        if (existingBook == null) {
//            inOutService.print(BOOK_DOES_NOT_EXIST_MSG);
//        } else {
//            Object[] params = {existingBook.getId(), existingBook.getTitle(), existingBook.getAuthor().getName(), existingBook.getGenre().getTitle()};
//            inOutService.print(StringUtils.arrayToDelimitedString(params, " "));
//        }
//    }

    public void deleteBook() {
        inOutService.print("Type book-id to delete from book list below:");
        List<Book> bookList = getBooks();

        if (bookList.isEmpty()) {
            return;
        }

        long id = inOutService.getUserIntInputMessage();

        Book existingBook = bookDao.findOneById(id);
        if (existingBook == null) {
            inOutService.print(BOOK_DOES_NOT_EXIST_MSG);
        } else {
            bookDao.deleteById(id);
            inOutService.print(BOOK_DELETED_MSG);
        }
    }

    public void updateBook() {
        inOutService.print("Type book-id to update from book list below:");
        List<Book> bookList = getBooks();

        if (!bookList.isEmpty()) {
            long id = inOutService.getUserIntInputMessage();
            Book existingBook = bookDao.findOneById(id);

            if (existingBook == null) {
                inOutService.print(BOOK_DOES_NOT_EXIST_MSG);
            } else {
                processUpdateExistingBook(existingBook);
            }
        }
    }

    private void processUpdateExistingBook(Book existingBook) {
        inOutService.print("Type new book title");
        String title = inOutService.getUserInputMessage();

        inOutService.print("Select new author-id from authors below:");
        List<Author> authorList = getAuthors();
        long authorId = inOutService.getUserIntInputMessage();

        inOutService.print("Select new genre-id from authors below:");
        List<Genre> genreList = getGenres();
        long genreId = inOutService.getUserIntInputMessage();

        boolean authorIdInRange = authorList.stream().map(Author::getId).collect(Collectors.toList()).contains(authorId);
        boolean genreIdInRange = genreList.stream().map(Genre::getId).collect(Collectors.toList()).contains(genreId);

        if (!authorIdInRange || !genreIdInRange) {
            inOutService.print(AUTHOR_OR_GENRE_DOES_NOT_EXIST_MSG);
            return;
        }
        if (existingBook.hasSameParams(title, authorId, genreId)) {
            inOutService.print(BOOK_ALREADY_EXISTS_MSG);
        } else {
            bookDao.update(existingBook.getId(), title, authorId, genreId);
            inOutService.print(BOOK_UPDATED_MSG);
        }
    }

    private List<Author> getAuthors() {
        List<Author> authorList = authorDao.findAll();
        authorList.forEach(item -> inOutService.print(item.getId() + " " + item.getName()));
        return authorList;
    }

    private List<Genre> getGenres() {
        List<Genre> genreList = genreDao.findAll();
        genreList.forEach(item -> inOutService.print(item.getId() + " " + item.getTitle()));
        return genreList;
    }

    public List<Book> getBooks() {
        List<Book> bookList = bookDao.findAll();
        if (bookList.isEmpty()) {
            inOutService.print(NO_BOOKS_AVAILABLE_MSG);
        } else {
            bookList.forEach(item -> {
                Object[] params = {item.getId(), item.getTitle(), item.getAuthor().getName(), item.getGenre().getTitle()};
                inOutService.print(StringUtils.arrayToDelimitedString(params, " "));
            });
        }
        return bookList;
    }
}
