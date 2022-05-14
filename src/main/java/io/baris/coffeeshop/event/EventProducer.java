package io.baris.coffeeshop.event;

import io.baris.coffeeshop.event.model.Command;
import io.baris.coffeeshop.event.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class EventProducer {

    private final EventConsumer eventConsumer;

    public <T> Optional<Event> createEvent(final Command<T> command) {
        var event = command.getEvent();
        eventConsumer.handleEvent(event);
        return Optional.of(event);
    }
}
