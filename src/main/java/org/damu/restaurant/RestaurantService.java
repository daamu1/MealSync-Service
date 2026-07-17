package org.damu.restaurant;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.damu.common.events.OrderCreatedEvent;
import org.damu.common.events.RestaurantAcceptedEvent;
import org.damu.kafka.MealSyncEventPublisher;
import org.damu.order.Order;
import org.damu.order.OrderNotFoundException;
import org.damu.order.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestaurantService {

	private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);

	private final OrderRepository orderRepository;
	private final MealSyncEventPublisher eventPublisher;

	public RestaurantService(OrderRepository orderRepository, MealSyncEventPublisher eventPublisher) {
		this.orderRepository = orderRepository;
		this.eventPublisher = eventPublisher;
	}

	@Transactional
	public void acceptOrder(OrderCreatedEvent event) {
		Order order = orderRepository.findById(event.orderId())
				.orElseThrow(() -> new OrderNotFoundException(event.orderId()));
		order.markAcceptedByRestaurant();

		Instant estimatedReadyAt = Instant.now().plus(20, ChronoUnit.MINUTES);
		log.info("Restaurant accepted order id={} restaurant={} estimatedReadyAt={}",
				order.getId(), order.getRestaurantName(), estimatedReadyAt);
		eventPublisher.publish(RestaurantAcceptedEvent.now(order.getId(), order.getRestaurantName(), estimatedReadyAt));
	}
}
