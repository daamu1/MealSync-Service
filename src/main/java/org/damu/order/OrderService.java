package org.damu.order;

import java.util.UUID;

import org.damu.common.events.OrderCreatedEvent;
import org.damu.kafka.MealSyncEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

	private static final Logger log = LoggerFactory.getLogger(OrderService.class);

	private final OrderRepository orderRepository;
	private final MealSyncEventPublisher eventPublisher;

	public OrderService(OrderRepository orderRepository, MealSyncEventPublisher eventPublisher) {
		this.orderRepository = orderRepository;
		this.eventPublisher = eventPublisher;
	}

	@Transactional
	public OrderResponse placeOrder(PlaceOrderRequest request) {
		Order order = Order.create(request.customerName(), request.restaurantName(), request.totalAmount());
		Order savedOrder = orderRepository.save(order);
		log.info("Order created id={} restaurant={} amount={}",
				savedOrder.getId(), savedOrder.getRestaurantName(), savedOrder.getTotalAmount());
		eventPublisher.publish(OrderCreatedEvent.now(
				savedOrder.getId(),
				savedOrder.getCustomerName(),
				savedOrder.getRestaurantName(),
				savedOrder.getTotalAmount()
		));
		return OrderResponse.from(savedOrder);
	}

	@Transactional(readOnly = true)
	public OrderResponse getOrder(UUID orderId) {
		return orderRepository.findById(orderId)
				.map(OrderResponse::from)
				.orElseThrow(() -> new OrderNotFoundException(orderId));
	}
}
