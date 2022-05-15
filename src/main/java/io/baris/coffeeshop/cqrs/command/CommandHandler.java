package io.baris.coffeeshop.cqrs.command;

import io.baris.coffeeshop.cqrs.event.EventManager;
import io.baris.coffeeshop.system.kafka.KafkaEventProducer;
import lombok.RequiredArgsConstructor;

/**
 * Handles commands to change the state of the application
 */
@RequiredArgsConstructor
public class CommandHandler {

    private final EventManager eventManager;
    private final KafkaEventProducer kafkaEventProducer;

    /**
     * 1. Saves the event in the database
     * 2. Publishes a message to the Kafka topic
     */
    public <T> void handleCommand(final Command<T> command) {
        eventManager.createEvent(command.getEvent());

        kafkaEventProducer.publishEvent(command);
    }
}
