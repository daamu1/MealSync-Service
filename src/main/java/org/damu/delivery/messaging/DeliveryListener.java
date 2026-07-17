package org.damu.delivery.messaging;

import org.damu.common.events.RestaurantAcceptedEvent;
import org.damu.delivery.application.DeliveryService;
import org.damu.kafka.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
class DeliveryListener {

	private final DeliveryService deliveryService;

	DeliveryListener(DeliveryService deliveryService) {
		this.deliveryService = deliveryService;
	}

	@KafkaListener(topics = KafkaTopics.RESTAURANT_ACCEPTED, groupId = "mealsync-delivery")
	void onRestaurantAccepted(RestaurantAcceptedEvent event) {
		deliveryService.assignRider(event);
	}
}
