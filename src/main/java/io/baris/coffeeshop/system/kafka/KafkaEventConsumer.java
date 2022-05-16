package io.baris.coffeeshop.system.kafka;

import io.baris.coffeeshop.cqrs.projection.StockProjector;
import io.baris.coffeeshop.cqrs.event.model.Event;
import io.baris.coffeeshop.cqrs.event.model.EventType;
import io.baris.coffeeshop.system.config.CoffeeShopConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.MockConsumer;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static io.baris.coffeeshop.system.kafka.KafkaUtils.getBootstrapServers;
import static io.baris.coffeeshop.system.kafka.KafkaUtils.getTopic;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.apache.kafka.clients.consumer.OffsetResetStrategy.NONE;

/**
 * Consumes events in kafka
 */
@Slf4j
@RequiredArgsConstructor
public class KafkaEventConsumer {

    private final Consumer<EventType, Event> consumer;
    private final StockProjector stockProjector;

    public KafkaEventConsumer(
        final StockProjector stockProjector,
        final CoffeeShopConfig config
    ) {
        this.stockProjector = stockProjector;
        this.consumer = config.testEnv()
            ? new MockConsumer<>(NONE)
            : new KafkaConsumer<>(getKafkaConsumerConfig());
    }

    public void subscribe() {
        consumer.subscribe(List.of(getTopic()));
        while (true) {
            consumer.poll(Duration.ofMillis(100))
                .forEach(record -> {
                    log.info("Event received for key={}, partition={}, offset={}, value={}",
                        record.key(), record.partition(), record.offset(), record.value()
                    );
                    stockProjector.projectData(record.value());
                });
        }
    }

    private static Map<String, Object> getKafkaConsumerConfig() {
        return Map.of(
            BOOTSTRAP_SERVERS_CONFIG, getBootstrapServers(),
            KEY_DESERIALIZER_CLASS_CONFIG, "io.baris.coffeeshop.system.kafka.serialization.EventTypeDeserializer",
            VALUE_DESERIALIZER_CLASS_CONFIG, "io.baris.coffeeshop.system.kafka.serialization.EventDeserializer",
            GROUP_ID_CONFIG, "test",
            AUTO_OFFSET_RESET_CONFIG, "earliest",
            ENABLE_AUTO_COMMIT_CONFIG, "true",
            AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000",
            SESSION_TIMEOUT_MS_CONFIG, "30000"
        );
    }
}
