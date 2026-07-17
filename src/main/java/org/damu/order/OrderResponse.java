package org.damu.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(
		UUID id,
		String customerName,
		String restaurantName,
		BigDecimal totalAmount,
		OrderStatus status,
		String riderName,
		Instant createdAt,
		Instant updatedAt
) {

	static OrderResponse from(Order order) {
		return new OrderResponse(
				order.getId(),
				order.getCustomerName(),
				order.getRestaurantName(),
				order.getTotalAmount(),
				order.getStatus(),
				order.getRiderName(),
				order.getCreatedAt(),
				order.getUpdatedAt()
		);
	}
}
