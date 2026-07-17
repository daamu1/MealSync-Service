package org.damu.notification;

import org.damu.common.events.DeliveryAssignedEvent;
import org.damu.kafka.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
class NotificationListener {

	private final NotificationService notificationService;

	NotificationListener(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@KafkaListener(topics = KafkaTopics.DELIVERY_ASSIGNED, groupId = "mealsync-notification")
	void onDeliveryAssigned(DeliveryAssignedEvent event) {
		notificationService.notifyDeliveryAssigned(event);
	}
}
