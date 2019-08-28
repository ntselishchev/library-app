package com.ntselishchev.libraryapp.service;

import com.ntselishchev.libraryapp.dao.AuthorDao;
import com.ntselishchev.libraryapp.dao.BookDao;
import com.ntselishchev.libraryapp.dao.CommentDao;
import com.ntselishchev.libraryapp.dao.GenreDao;
import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Comment;
import com.ntselishchev.libraryapp.domain.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final InOutService inOutService;
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final CommentDao commentDao;

    private static final String BOOK_ALREADY_EXISTS_MSG = "Book having provided parameters already exists";
    private static final String BOOK_SAVED_MSG = "Book has been added";
    private static final String BOOK_DELETED_MSG = "Book has been deleted";
    private static final String BOOK_UPDATED_MSG = "Book has been updated";
    private static final String BOOK_DOES_NOT_EXIST_MSG = "Book having provided id does not exist";
    private static final String AUTHOR_OR_GENRE_DOES_NOT_EXIST_MSG = "Selected author-id or genre-id does not exist";
    private static final String NO_BOOKS_AVAILABLE_MSG = "There are no saved books yet";
    private static final String NO_BOOK_COMMENTS_AVAILABLE_MSG = "There are no saved comments related to this book yet";
    private static final String COMMENT_DOES_NOT_EXIST_MSG = "There is no comment having provided id exist";

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
        Optional<Author> author = authorDao.findById(authorId);
        Optional<Genre> genre = genreDao.findById(genreId);

        author.ifPresent(a ->
                genre.ifPresent(g -> {
                    Book newBook = new Book(title, a, g);
                    bookDao.save(newBook);
                    inOutService.print(BOOK_SAVED_MSG);
        }));
    }

    public void deleteBook() {
        inOutService.print("Type book-id to delete from book list below:");
        List<Book> bookList = getBooks();

        if (bookList.isEmpty()) {
            return;
        }

        long id = inOutService.getUserIntInputMessage();
        Optional<Book> existingBook = bookList.stream().filter(book -> id == book.getId()).findFirst();

        if (!existingBook.isPresent()) {
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
            Optional<Book> existingBook = bookList.stream().filter(book -> id == book.getId()).findFirst();

            if (!existingBook.isPresent()) {
                inOutService.print(BOOK_DOES_NOT_EXIST_MSG);
            } else {
                processUpdateExistingBook(existingBook.get());
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
            Optional<Author> author = authorDao.findById(authorId);
            Optional<Genre> genre = genreDao.findById(genreId);

            author.ifPresent(a ->
                    genre.ifPresent(g -> {
                        existingBook.setTitle(title);
                        existingBook.setAuthor(a);
                        existingBook.setGenre(g);
                        bookDao.save(existingBook);
                        inOutService.print(BOOK_UPDATED_MSG);
                    }));
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

    public void addComment() {
        inOutService.print("Type book-id to leave comment from book list below:");
        List<Book> bookList = getBooks();

        if (!bookList.isEmpty()) {
            long id = inOutService.getUserIntInputMessage();
            Optional<Book> existingBook = bookList.stream().filter(book -> id == book.getId()).findFirst();

            if (!existingBook.isPresent()) {
                inOutService.print(BOOK_DOES_NOT_EXIST_MSG);
            } else {
                processAddCommentToExistingBook(existingBook.get());
            }
        }

    }

    private void processAddCommentToExistingBook(Book existingBook) {
        inOutService.print("Type your comment");
        String content = inOutService.getUserInputMessage();
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setBook(existingBook);
        commentDao.save(comment);
    }

    public void getBookComments() {
        inOutService.print("Type book-id to see its comments from book list below:");
        List<Book> bookList = getBooks();

        if (!bookList.isEmpty()) {
            long id = inOutService.getUserIntInputMessage();
            Optional<Book> existingBook = bookList.stream().filter(book -> id == book.getId()).findFirst();

            if (!existingBook.isPresent()) {
                inOutService.print(BOOK_DOES_NOT_EXIST_MSG);
            } else {
                processGetBookCommentsOfExistingBook(existingBook.get());
            }
        }
    }

    private void processGetBookCommentsOfExistingBook(Book existingBook) {
        List<Comment> comments = commentDao.findAllByBook(existingBook);

        if (comments.isEmpty()) {
            inOutService.print(NO_BOOK_COMMENTS_AVAILABLE_MSG);
        } else {
            comments.forEach(comment -> inOutService.print(comment.getContent()));
        }
    }

    public void deleteComment() {
        inOutService.print("Type book-id to delete comment from book list below:");
        List<Book> bookList = getBooks();

        if (!bookList.isEmpty()) {
            long id = inOutService.getUserIntInputMessage();
            Optional<Book> existingBook = bookList.stream().filter(book -> id == book.getId()).findFirst();

            if (!existingBook.isPresent()) {
                inOutService.print(BOOK_DOES_NOT_EXIST_MSG);
            } else {
                processDeleteBookCommentsOfExistingBook(existingBook.get());
            }
        }

    }

    private void processDeleteBookCommentsOfExistingBook(Book existingBook) {
        List<Comment> comments = commentDao.findAllByBook(existingBook);

        if (comments.isEmpty()) {
            inOutService.print(NO_BOOK_COMMENTS_AVAILABLE_MSG);
        } else {
            inOutService.print("Type comment-id to delete from book list below:");
            comments.forEach(comment -> inOutService.print(comment.getId() + " " + comment.getContent()));
            long id = inOutService.getUserIntInputMessage();

            Optional<Comment> commentToDelete = comments.stream().filter(comment -> comment.getId() == id).findFirst();

            if (commentToDelete.isPresent()) {
                commentDao.delete(commentToDelete.get());
            } else {
                inOutService.print(COMMENT_DOES_NOT_EXIST_MSG);
            }
        }
    }
}
