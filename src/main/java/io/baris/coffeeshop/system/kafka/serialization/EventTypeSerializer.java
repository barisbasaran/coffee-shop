package io.baris.coffeeshop.system.kafka.serialization;

import io.baris.coffeeshop.cqrs.event.model.EventType;
import org.apache.kafka.common.serialization.Serializer;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Serializer for event type to use with kafka
 */
public class EventTypeSerializer implements Serializer<EventType> {

    @Override
    public byte[] serialize(String topic, EventType data) {
        return data.toString().getBytes(UTF_8);
    }
}
