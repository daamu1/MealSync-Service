package org.damu.order.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order {

	@Id
	private UUID id;

	@Column(nullable = false, length = 120)
	private String customerName;

	@Column(nullable = false, length = 120)
	private String restaurantName;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal totalAmount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 40)
	private OrderStatus status;

	@Column(length = 120)
	private String riderName;

	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	@Column(nullable = false)
	private Instant updatedAt;

	@Version
	private long version;

	protected Order() {
	}

	private Order(UUID id, String customerName, String restaurantName, BigDecimal totalAmount, OrderStatus status) {
		this.id = id;
		this.customerName = customerName;
		this.restaurantName = restaurantName;
		this.totalAmount = totalAmount;
		this.status = status;
	}

	public static Order create(String customerName, String restaurantName, BigDecimal totalAmount) {
		return new Order(UUID.randomUUID(), customerName, restaurantName, totalAmount, OrderStatus.CREATED);
	}

	public void markAcceptedByRestaurant() {
		if (status == OrderStatus.CREATED) {
			status = OrderStatus.RESTAURANT_ACCEPTED;
		}
	}

	public void assignDelivery(String riderName) {
		if (status == OrderStatus.RESTAURANT_ACCEPTED) {
			this.riderName = riderName;
			status = OrderStatus.DELIVERY_ASSIGNED;
		}
	}

	@PrePersist
	void onCreate() {
		Instant now = Instant.now();
		createdAt = now;
		updatedAt = now;
	}

	@PreUpdate
	void onUpdate() {
		updatedAt = Instant.now();
	}

}
