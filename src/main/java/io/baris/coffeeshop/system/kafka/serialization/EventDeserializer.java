package io.baris.coffeeshop.system.kafka.serialization;

import io.baris.coffeeshop.cqrs.event.model.Event;
import org.apache.kafka.common.serialization.Deserializer;

import static io.baris.coffeeshop.system.utils.SystemUtils.toObject;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Deserializer for event to use with kafka
 */
public class EventDeserializer implements Deserializer<Event> {

    @Override
    public Event deserialize(String topic, byte[] data) {
        return toObject(new String(data, UTF_8), Event.class);
    }
}
