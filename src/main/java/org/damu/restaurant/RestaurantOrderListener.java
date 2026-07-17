package org.damu.restaurant;

import org.damu.common.events.OrderCreatedEvent;
import org.damu.kafka.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
class RestaurantOrderListener {

	private final RestaurantService restaurantService;

	RestaurantOrderListener(RestaurantService restaurantService) {
		this.restaurantService = restaurantService;
	}

	@KafkaListener(topics = KafkaTopics.ORDER_CREATED, groupId = "mealsync-restaurant")
	void onOrderCreated(OrderCreatedEvent event) {
		restaurantService.acceptOrder(event);
	}
}
