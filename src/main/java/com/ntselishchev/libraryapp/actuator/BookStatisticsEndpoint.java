package com.ntselishchev.libraryapp.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Endpoint(id = "bookstats")
public class BookStatisticsEndpoint {

    private final MeterRegistry metric;

    @ReadOperation
    public Map<String, Double> bookStats() {
        Map<String, Double> stats = new HashMap<>();
        stats.put("booksCreated", metric.counter("books.created").count());
        stats.put("booksDeleted", metric.counter("books.deleted").count());
        stats.put("booksUpdated", metric.counter("books.updated").count());
        return stats;
    }

}