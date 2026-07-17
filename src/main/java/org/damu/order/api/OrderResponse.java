package org.damu.order.api;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.damu.order.domain.Order;
import org.damu.order.domain.OrderStatus;

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

	public static OrderResponse from(Order order) {
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
