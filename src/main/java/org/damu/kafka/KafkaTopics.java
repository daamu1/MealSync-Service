package org.damu.kafka;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaTopics {
	public final String ORDER_CREATED = "mealsync.order.created";
	public final String RESTAURANT_ACCEPTED = "mealsync.restaurant.accepted";
	public final String DELIVERY_ASSIGNED = "mealsync.delivery.assigned";
}