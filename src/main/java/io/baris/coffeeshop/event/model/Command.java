package io.baris.coffeeshop.event.model;

import java.time.Instant;

import static io.baris.coffeeshop.system.SystemUtils.toJsonString;

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
