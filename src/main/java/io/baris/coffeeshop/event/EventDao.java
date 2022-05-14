package io.baris.coffeeshop.event;

import io.baris.coffeeshop.event.model.Event;
import io.baris.coffeeshop.event.model.EventType;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Manages pets in the database
 */
public interface EventDao {

    @SqlQuery("SELECT * FROM events ORDER BY event_time")
    @RegisterBeanMapper(Event.class)
    List<Event> getAllEvents();

    @SqlUpdate("INSERT INTO events (event_time, event_type, event) VALUES (?, ?, ?)")
    void createEvent(Instant eventTime, EventType eventType, String event);

    @Transaction
    default Optional<Event> createEvent(final Event event) {
        createEvent(
            event.getEventTime(),
            event.getEventType(),
            event.getEvent()
        );
        return Optional.of(event);
    }
}
