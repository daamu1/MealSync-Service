package org.damu.notification.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.damu.common.events.DeliveryAssignedEvent;
import org.damu.kafka.KafkaTopics;
import org.damu.notification.application.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@RequiredArgsConstructor
class NotificationListener {

	private final NotificationService notificationService;

	@KafkaListener(topics = KafkaTopics.DELIVERY_ASSIGNED, groupId = "mealsync-notification")
	void onDeliveryAssigned(DeliveryAssignedEvent event) {
		log.info("Received Delivery Assigned event: {}", event);
		notificationService.notifyDeliveryAssigned(event);
	}
}
