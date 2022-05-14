package io.baris.coffeeshop.event.model;

public interface Command<T> {

    EventType getEventType();

    T getEventObject();
}
