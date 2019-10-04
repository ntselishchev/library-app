package com.ntselishchev.libraryapp.actuator;

import com.ntselishchev.libraryapp.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LibraryServiceHealthIndicator implements HealthIndicator {

    private final LibraryService libraryService;

    @Override
    public Health health() {
        try {
            int bookSize = libraryService.getBooks().size();
            return Health.up().withDetail("bookSize", bookSize).build();
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}