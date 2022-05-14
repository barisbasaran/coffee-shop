package io.baris.coffeeshop.event;

import io.baris.coffeeshop.event.model.Command;
import io.baris.coffeeshop.event.model.Event;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static io.baris.coffeeshop.system.SystemUtils.toJsonString;

/**
 * Manages the pet
 */
@RequiredArgsConstructor
public class EventManager {

    private final Jdbi jdbi;

    public List<Event> getAllEvents() {
        return jdbi.withExtension(EventDao.class, EventDao::getAllEvents);
    }

    public <T> Optional<Event> addEvent(final Command<T> command) {
        var event = Event.builder()
            .eventType(command.getEventType())
            .eventTime(Instant.now())
            .event(toJsonString(command.getEventObject()))
            .build();
        return jdbi.withExtension(EventDao.class, dao -> dao.createEvent(event));
    }
}
