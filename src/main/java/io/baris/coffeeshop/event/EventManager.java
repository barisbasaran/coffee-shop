package io.baris.coffeeshop.event;

import io.baris.coffeeshop.event.model.Event;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;

/**
 * Manages the pet
 */
@RequiredArgsConstructor
public class EventManager {

    private final Jdbi jdbi;

    public List<Event> getAllEvents() {
        return jdbi.withExtension(EventRepository.class, EventRepository::getAllEvents);
    }

    public Optional<Event> createEvent(final Event event) {
        return jdbi.withExtension(EventRepository.class, dao -> dao.createEvent(event));
    }
}
