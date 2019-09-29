package com.ntselishchev.libraryapp.changelogs;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ChangeLog
public class Changelog {

    private static final String AUTHORS = "authors";
    private static final String GENRES = "genres";
    private static final String BOOKS = "books";
    private static final String COMMENTS = "comments";
    private static final String USERS = "users";
    private static final String ACL = "ACL";

    private static final int ACL_PERMISSION_READ = 1;

    @ChangeSet(order = "001", id = "insertDefaultData", author = "n.tselishchev")
    public void insertDefaultData(MongoDatabase db) {
        MongoCollection<Document> authorCollection = db.getCollection(AUTHORS);
        Document authorDoc1 = new Document("name", "author 1");
        Document authorDoc2 = new Document("name", "author 2");
        Document authorDoc3 = new Document("name", "author 3");
        Document authorDoc4 = new Document("name", "author 4");
        Document authorDoc5 = new Document("name", "author 5");
        authorCollection.insertMany(Arrays.asList(authorDoc1, authorDoc2, authorDoc3, authorDoc4, authorDoc5));
        
        MongoCollection<Document> genreCollection = db.getCollection(GENRES);
        Document genreDoc1 = new Document("title", "genre 1");
        Document genreDoc2 = new Document("title", "genre 2");
        Document genreDoc3 = new Document("title", "genre 3");
        Document genreDoc4 = new Document("title", "genre 4");
        Document genreDoc5 = new Document("title", "genre 5");
        genreCollection.insertMany(Arrays.asList(genreDoc1, genreDoc2, genreDoc3, genreDoc4, genreDoc5));
        
        MongoCollection<Document> bookCollection = db.getCollection(BOOKS);
        Document bookDoc1 = new Document("title", "book 1")
                .append("author", getRef(AUTHORS, authorDoc1))
                .append("genre", getRef(GENRES, genreDoc2));
        Document bookDoc2 = new Document("title", "book 2")
                .append("author", getRef(AUTHORS, authorDoc3))
                .append("genre", getRef(GENRES, genreDoc3));
        Document bookDoc3 = new Document("title", "book 3")
                .append("author", getRef(AUTHORS, authorDoc5))
                .append("genre", getRef(GENRES, genreDoc4));
        Document bookDoc4 = new Document("title", "book 4")
                .append("author", getRef(AUTHORS, authorDoc4))
                .append("genre", getRef(GENRES, genreDoc5));
        Document bookDoc5 = new Document("title", "book 5")
                .append("author", getRef(AUTHORS, authorDoc3))
                .append("genre", getRef(GENRES, genreDoc2));
        bookCollection.insertMany(Arrays.asList(bookDoc1, bookDoc2, bookDoc3, bookDoc4, bookDoc5));

        MongoCollection<Document> commentCollection = db.getCollection(COMMENTS);
        Document commentDoc1 = new Document("content", "comment 1").append("book", getRef(BOOKS, bookDoc1));
        Document commentDoc2 = new Document("content", "comment 2").append("book", getRef(BOOKS, bookDoc1));
        Document commentDoc3 = new Document("content", "comment 3").append("book", getRef(BOOKS, bookDoc2));
        Document commentDoc4 = new Document("content", "comment 4").append("book", getRef(BOOKS, bookDoc3));
        Document commentDoc5 = new Document("content", "comment 5").append("book", getRef(BOOKS, bookDoc2));
        commentCollection.insertMany(Arrays.asList(commentDoc1, commentDoc2, commentDoc3, commentDoc4, commentDoc5));

        MongoCollection<Document> aclCollection = db.getCollection(ACL);

        Document ownerDoc = new Document("name", "admin").append("isPrincipal", true);
        Document sidDoc = new Document("name", "admin").append("isPrincipal", true);
        Document permissionDoc = new Document("sid", sidDoc)
                .append("permission", ACL_PERMISSION_READ)
                .append("granting", true)
                .append("auditFailure", true)
                .append("auditSuccess", true);
        List<Document> permissionDocs = Collections.singletonList(permissionDoc);
        Document acl = new Document("_class", "org.springframework.security.acls.domain.MongoAcl")
                .append("className", "com.ntselishchev.libraryapp.domain.Book")
                .append("instanceId", bookDoc1.get("_id").toString())
                .append("owner", ownerDoc)
                .append("inheritPermissions", true)
                .append("permissions", permissionDocs);

        Document ownerDoc2 = new Document("name", "admin").append("isPrincipal", true);
        Document sidDoc2 = new Document("name", "admin").append("isPrincipal", true);
        Document permissionDoc2 = new Document("sid", sidDoc2)
                .append("permission", ACL_PERMISSION_READ)
                .append("granting", true)
                .append("auditFailure", true)
                .append("auditSuccess", true);
        List<Document> permissionDocs2 = Collections.singletonList(permissionDoc2);
        Document acl2 = new Document("_class", "org.springframework.security.acls.domain.MongoAcl")
                .append("className", "com.ntselishchev.libraryapp.domain.Book")
                .append("instanceId", bookDoc2.get("_id").toString())
                .append("owner", ownerDoc2)
                .append("inheritPermissions", true)
                .append("permissions", permissionDocs2);

        aclCollection.insertMany(Arrays.asList(acl, acl2));
        
    }

    @ChangeSet(order = "002", id = "insertUsers", author = "n.tselishchev")
    public void insertUsers(MongoDatabase db) {
        MongoCollection<Document> authorCollection = db.getCollection(USERS);
        Document userDoc1 = new Document("userName", "admin").append("password", "password")
                .append("expired", false).append("locked", false).append("credentialsExpired", false)
                .append("enabled", true).append("role", "USER");
        Document userDoc2 = new Document("userName", "expired").append("password", "password")
                .append("expired", true).append("locked", false).append("credentialsExpired", false)
                .append("enabled", true).append("role", "USER");
        Document userDoc3 = new Document("userName", "locked").append("password", "password")
                .append("expired", false).append("locked", true).append("credentialsExpired", false)
                .append("enabled", true).append("role", "USER");
        Document userDoc4 = new Document("userName", "somebody").append("password", "password")
                .append("expired", false).append("locked", false).append("credentialsExpired", false)
                .append("enabled", true).append("role", "USER");
        authorCollection.insertMany(Arrays.asList(userDoc1, userDoc2, userDoc3, userDoc4));
    }

    private Document getRef(String collection, Document doc) {
        return new Document("$ref", collection).append("$id", doc.get("_id"));
    }
}
