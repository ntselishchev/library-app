package com.ntselishchev.libraryapp.batch;

import com.ntselishchev.libraryapp.domain.Author;
import com.ntselishchev.libraryapp.domain.Book;
import com.ntselishchev.libraryapp.domain.Genre;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
@Slf4j
public class FlowConfig  extends DefaultBatchConfigurer {

    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final DataSource dataSource;
    private final MongoTemplate template;


    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Author, Author>chunk(3)
                .reader(authorReader())
                .processor(authorProcessor())
                .writer(authorWriter())
                .build();
    }

    @Bean
    public MongoItemReader<Author> authorReader() {
        MongoItemReader<Author> reader = new MongoItemReader<>();
        reader.setTemplate(template);
        reader.setCollection("authors");
        reader.setTargetType(Author.class);
        reader.setQuery("{}");
        reader.setSort(Collections.emptyMap());

        return reader;
    }

    @Bean
    public ItemProcessor<Author, Author> authorProcessor() {
        return author -> author;
    }

    @Bean
    public JdbcBatchItemWriter<Author> authorWriter() {
        JdbcBatchItemWriter<Author> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(
                new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO authors " +
                "(name) " +
                "VALUES (:name)");
        writer.setDataSource(dataSource);

        return writer;
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .<Genre, Genre>chunk(3)
                .reader(genreReader())
                .processor(genreProcessor())
                .writer(genreWriter())
                .build();
    }

    @Bean
    public MongoItemReader<Genre> genreReader() {
        MongoItemReader<Genre> reader = new MongoItemReader<>();
        reader.setTemplate(template);
        reader.setCollection("genres");
        reader.setTargetType(Genre.class);
        reader.setQuery("{}");
        reader.setSort(Collections.emptyMap());

        return reader;
    }

    @Bean
    public ItemProcessor<Genre, Genre> genreProcessor() {
        return genre -> genre;
    }

    @Bean
    public JdbcBatchItemWriter<Genre> genreWriter() {
        JdbcBatchItemWriter<Genre> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(
                new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO genres " +
                "(title) " +
                "VALUES (:title)");
        writer.setDataSource(dataSource);

        return writer;
    }

    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .<Book, Book>chunk(3)
                .reader(bookReader())
                .processor(bookProcessor())
                .writer(bookWriter())
                .build();
    }

    @Bean
    public MongoItemReader<Book> bookReader() {
        MongoItemReader<Book> reader = new MongoItemReader<>();
        reader.setTemplate(template);
        reader.setCollection("books");
        reader.setTargetType(Book.class);
        reader.setQuery("{}");
        reader.setSort(Collections.emptyMap());

        return reader;
    }

    @Bean
    public ItemProcessor<Book, Book> bookProcessor() {
        return book -> book;
    }

    @Bean
    public JdbcBatchItemWriter<Book> bookWriter() {
        JdbcBatchItemWriter<Book> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(
                new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO books " +
                "(title, author_id, genre_id) " +
                "SELECT " +
                "   cast(:title as text) as title, " +
                "   a.id as author_id, " +
                "   g.id as genre_id " +
                "from authors a " +
                "join genres g on g.title = :genre.title " +
                "where a.name = :author.name ");
        writer.setDataSource(dataSource);

        return writer;
    }

    @Bean
    public Job importBooksJob(Step step1, Step step2, Step step3) {
        return jobBuilderFactory
                .get("importBooksJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .next(step2)
                .next(step3)
                .end()
                .build();
    }

}
