package org.damu.kafka;

public final class KafkaTopics {

	public static final String ORDER_CREATED = "mealsync.order.created";
	public static final String RESTAURANT_ACCEPTED = "mealsync.restaurant.accepted";
	public static final String DELIVERY_ASSIGNED = "mealsync.delivery.assigned";

	private KafkaTopics() {
	}
}
