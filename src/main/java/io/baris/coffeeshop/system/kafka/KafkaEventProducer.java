package io.baris.coffeeshop.system.kafka;

import io.baris.coffeeshop.cqrs.command.Command;
import io.baris.coffeeshop.cqrs.event.model.Event;
import io.baris.coffeeshop.cqrs.event.model.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Map;

import static io.baris.coffeeshop.system.kafka.KafkaUtils.getBootstrapServers;
import static io.baris.coffeeshop.system.kafka.KafkaUtils.getTopic;
import static org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

/**
 * Produces events in kafka
 */
@Slf4j
@RequiredArgsConstructor
public class KafkaEventProducer {

    private final Producer<EventType, Event> producer;

    public static Producer<EventType, Event> createKafkaProducer() {
        return new KafkaProducer<>(getKafkaProducerConfig());
    }

    public <T> void publishEvent(final Command<T> command) {
        var event = command.getEvent();

        var record = new ProducerRecord<>(getTopic(), event.getEventType(), event);
        producer.send(record);

        log.info("Event sent for {}", event);
    }

    private static Map<String, Object> getKafkaProducerConfig() {
        return Map.of(
            BOOTSTRAP_SERVERS_CONFIG, getBootstrapServers(),
            KEY_SERIALIZER_CLASS_CONFIG, "io.baris.coffeeshop.system.kafka.serialization.EventTypeSerializer",
            VALUE_SERIALIZER_CLASS_CONFIG, "io.baris.coffeeshop.system.kafka.serialization.EventSerializer",
            ACKS_CONFIG, "1"
        );
    }
}
