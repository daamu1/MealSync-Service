package org.damu.delivery;

import org.damu.common.events.DeliveryAssignedEvent;
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
public class DeliveryService {

	private static final Logger log = LoggerFactory.getLogger(DeliveryService.class);

	private static final String DEFAULT_RIDER = "Rider-101";

	private final OrderRepository orderRepository;
	private final DeliveryAssignmentRepository deliveryAssignmentRepository;
	private final MealSyncEventPublisher eventPublisher;

	public DeliveryService(
			OrderRepository orderRepository,
			DeliveryAssignmentRepository deliveryAssignmentRepository,
			MealSyncEventPublisher eventPublisher
	) {
		this.orderRepository = orderRepository;
		this.deliveryAssignmentRepository = deliveryAssignmentRepository;
		this.eventPublisher = eventPublisher;
	}

	@Transactional
	public void assignRider(RestaurantAcceptedEvent event) {
		Order order = orderRepository.findById(event.orderId())
				.orElseThrow(() -> new OrderNotFoundException(event.orderId()));

		DeliveryAssignment assignment = deliveryAssignmentRepository.findById(event.orderId())
				.orElseGet(() -> deliveryAssignmentRepository.save(DeliveryAssignment.create(event.orderId(), DEFAULT_RIDER)));

		order.assignDelivery(assignment.getRiderName());
		log.info("Delivery assigned orderId={} rider={}", order.getId(), assignment.getRiderName());
		eventPublisher.publish(DeliveryAssignedEvent.now(order.getId(), assignment.getRiderName()));
	}
}
