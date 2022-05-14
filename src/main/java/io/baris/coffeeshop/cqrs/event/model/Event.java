package io.baris.coffeeshop.cqrs.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Represents an event
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    private Instant eventTime;
    private EventType eventType;
    private String event;
}
