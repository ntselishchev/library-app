package com.ntselishchev.libraryapp.events;

import com.ntselishchev.libraryapp.dao.BookDao;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongoBookEventListener extends AbstractMongoEventListener<Book> {

    private final BookDao bookDao;

    private final ReactiveMongoTemplate template;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        super.onBeforeDelete(event);
        Document document = event.getSource();
        String id = document.get("_id").toString();

        bookDao.findById(id).then(template.remove(Query.query(Criteria.where("book").is(id)), Comment.class)).subscribe();
    }
}
