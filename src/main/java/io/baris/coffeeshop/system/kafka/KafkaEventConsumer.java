package io.baris.coffeeshop.system.kafka;

import io.baris.coffeeshop.event.EventManager;
import io.baris.coffeeshop.event.model.Event;
import io.baris.coffeeshop.event.model.EventType;
import io.baris.coffeeshop.inventory.InventoryManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static io.baris.coffeeshop.event.EventMapper.mapToAddStock;
import static io.baris.coffeeshop.event.EventMapper.mapToShoppingCart;
import static io.baris.coffeeshop.system.kafka.KafkaUtils.getBootstrapServers;
import static io.baris.coffeeshop.system.kafka.KafkaUtils.getTopic;

/**
 * Subscribes to kafka topics to receive events
 */
@Slf4j
@RequiredArgsConstructor
public class KafkaEventConsumer {

    private final Consumer<EventType, Event> consumer;
    private final EventManager eventManager;
    private final InventoryManager inventoryManager;

    public static Consumer<EventType, Event> createKafkaConsumer() {
        return new KafkaConsumer<>(getKafkaConsumerConfig());
    }

    public void subscribe() {
        consumer.subscribe(List.of(getTopic()));
        while (true) {
            var records = consumer.poll(Duration.ofMillis(100));
            for (var record : records) {
                log.info("Event received for key={}, partition={}, offset={}, value={}",
                    record.key(), record.partition(), record.offset(), record.value()
                );
                eventManager.createEvent(record.value());

                switch (record.key()) {
                    case CHECKOUT -> inventoryManager
                        .updateInventory(mapToShoppingCart(record.value()));
                    case ADD_STOCK -> inventoryManager
                        .updateInventory(mapToAddStock(record.value()));
                    default -> log.info("No service found for key={}", record.key());
                }
            }
        }
    }

    private static Map<String, Object> getKafkaConsumerConfig() {
        return Map.of(
            "bootstrap.servers", getBootstrapServers(),
            "key.deserializer", "io.baris.coffeeshop.system.kafka.serialization.EventTypeDeserializer",
            "value.deserializer", "io.baris.coffeeshop.system.kafka.serialization.EventDeserializer",
            "group.id", "test",
            "auto.offset.reset", "earliest",
            "enable.auto.commit", "true",
            "auto.commit.interval.ms", "1000",
            "session.timeout.ms", "30000"
        );
    }
}
