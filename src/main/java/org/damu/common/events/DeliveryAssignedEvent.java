package org.damu.common.events;

import java.time.Instant;
import java.util.UUID;

public record DeliveryAssignedEvent(
		UUID eventId,
		Instant occurredAt,
		int schemaVersion,
		UUID orderId,
		String riderName
) {

	public static DeliveryAssignedEvent now(UUID orderId, String riderName) {
		return new DeliveryAssignedEvent(UUID.randomUUID(), Instant.now(), 1, orderId, riderName);
	}
}
