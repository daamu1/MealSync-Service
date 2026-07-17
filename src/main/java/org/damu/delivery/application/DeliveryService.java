package org.damu.delivery.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.damu.common.events.DeliveryAssignedEvent;
import org.damu.common.events.RestaurantAcceptedEvent;
import org.damu.delivery.domain.DeliveryAssignment;
import org.damu.delivery.infrastructure.DeliveryAssignmentRepository;
import org.damu.kafka.MealSyncEventPublisher;
import org.damu.order.domain.Order;
import org.damu.order.exception.OrderNotFoundException;
import org.damu.order.infrastructure.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {
    private static final String DEFAULT_RIDER = "Rider-101";
    private final OrderRepository orderRepository;
    private final DeliveryAssignmentRepository deliveryAssignmentRepository;
    private final MealSyncEventPublisher eventPublisher;


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
