package io.baris.coffeeshop.system.kafka.serialization;

import io.baris.coffeeshop.event.model.Event;
import io.baris.coffeeshop.system.utils.SystemUtils;
import org.apache.kafka.common.serialization.Serializer;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Serializer for event type to use with kafka
 */
public class EventSerializer implements Serializer<Event> {

    @Override
    public byte[] serialize(String topic, Event data) {
        return SystemUtils.toJsonString(data).getBytes(UTF_8);
    }
}
