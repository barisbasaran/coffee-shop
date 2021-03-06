package io.baris.coffeeshop.cqrs.command;

import io.baris.coffeeshop.cqrs.event.model.Event;
import io.baris.coffeeshop.cqrs.event.model.EventType;

import java.time.Instant;

import static io.baris.coffeeshop.system.utils.SystemUtils.toJsonString;

/**
 * Represents a command to change the state of the application
 *
 * @param <T>
 */
public interface Command<T> {

    EventType getEventType();

    T getEventObject();

    default Event getEvent() {
        return Event.builder()
            .eventType(getEventType())
            .eventTime(Instant.now())
            .event(toJsonString(getEventObject()))
            .build();
    }
}
