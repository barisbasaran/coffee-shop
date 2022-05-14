package io.baris.coffeeshop.cqrs.command;

import io.baris.coffeeshop.cqrs.event.EventManager;
import io.baris.coffeeshop.system.kafka.KafkaEventProducer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommandHandler {

    private final EventManager eventManager;
    private final KafkaEventProducer kafkaEventProducer;

    public <T> void handleCommand(final Command<T> command) {
        eventManager.createEvent(command.getEvent());

        kafkaEventProducer.publishEvent(command);
    }
}
