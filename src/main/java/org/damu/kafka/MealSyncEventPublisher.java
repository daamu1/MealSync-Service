package org.damu.kafka;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.damu.common.events.DeliveryAssignedEvent;
import org.damu.common.events.OrderCreatedEvent;
import org.damu.common.events.RestaurantAcceptedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@RequiredArgsConstructor
public class MealSyncEventPublisher {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	public void publish(OrderCreatedEvent event) {
		publish(KafkaTopics.ORDER_CREATED, event.orderId(), event);
	}

	public void publish(RestaurantAcceptedEvent event) {
		publish(KafkaTopics.RESTAURANT_ACCEPTED, event.orderId(), event);
	}

	public void publish(DeliveryAssignedEvent event) {
		publish(KafkaTopics.DELIVERY_ASSIGNED, event.orderId(), event);
	}

	private void publish(String topic, UUID aggregateId, Object event) {
		log.debug("Publishing event topic={} aggregateId={} payloadType={}", topic, aggregateId, event.getClass().getSimpleName());
		kafkaTemplate.send(topic, aggregateId.toString(), event);
	}
}
