package org.damu.common.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderCreatedEvent(UUID eventId, Instant occurredAt, int schemaVersion, UUID orderId, String customerName,
                                String restaurantName, BigDecimal totalAmount) {
    public static OrderCreatedEvent now(UUID orderId, String customerName, String restaurantName, BigDecimal totalAmount) {
        return new OrderCreatedEvent(UUID.randomUUID(), Instant.now(), 1, orderId, customerName, restaurantName, totalAmount);
    }
}
