package org.damu.common.events;

import java.time.Instant;
import java.util.UUID;

public record RestaurantAcceptedEvent(
		UUID eventId,
		Instant occurredAt,
		int schemaVersion,
		UUID orderId,
		String restaurantName,
		Instant estimatedReadyAt
) {

	public static RestaurantAcceptedEvent now(UUID orderId, String restaurantName, Instant estimatedReadyAt) {
		return new RestaurantAcceptedEvent(UUID.randomUUID(), Instant.now(), 1, orderId, restaurantName, estimatedReadyAt);
	}
}
