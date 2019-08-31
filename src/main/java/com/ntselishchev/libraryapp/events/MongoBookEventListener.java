package com.ntselishchev.libraryapp.events;

import com.ntselishchev.libraryapp.dao.BookDao;
import com.ntselishchev.libraryapp.dao.CommentDao;
import com.ntselishchev.libraryapp.domain.Book;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MongoBookEventListener extends AbstractMongoEventListener<Book> {

    private final CommentDao commentDao;
    private final BookDao bookDao;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        super.onBeforeDelete(event);
        Document document = event.getSource();
        String id = document.get("_id").toString();
        Optional<Book> book = bookDao.findById(id);
        book.ifPresent(commentDao::deleteAllByBook);
    }
}
