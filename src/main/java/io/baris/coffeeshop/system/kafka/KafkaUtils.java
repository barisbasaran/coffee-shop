package io.baris.coffeeshop.system.kafka;

import java.util.Optional;

/**
 * Utilities for kafka
 */
public class KafkaUtils {

    public static final String KAFKA_URL = "localhost:9093";
    public static final String MY_TOPIC = "mytopic";

    public static String getTopic() {
        return MY_TOPIC;
    }

    public static String getBootstrapServers() {
        return Optional
            .ofNullable(System.getenv("KAFKA_URL"))
            .orElse(KAFKA_URL);
    }
}
