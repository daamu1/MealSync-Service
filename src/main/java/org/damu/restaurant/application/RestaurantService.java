package org.damu.restaurant.application;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.damu.common.events.OrderCreatedEvent;
import org.damu.common.events.RestaurantAcceptedEvent;
import org.damu.kafka.MealSyncEventPublisher;
import org.damu.order.domain.Order;
import org.damu.order.exception.OrderNotFoundException;
import org.damu.order.infrastructure.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final OrderRepository orderRepository;
    private final MealSyncEventPublisher eventPublisher;

    @Transactional
    public void acceptOrder(OrderCreatedEvent event) {
        Order order = orderRepository.findById(event.orderId())
                .orElseThrow(() -> new OrderNotFoundException(event.orderId()));
        order.markAcceptedByRestaurant();
        Instant estimatedReadyAt = Instant.now().plus(20, ChronoUnit.MINUTES);
        log.info("Restaurant accepted order id={} restaurant={} estimatedReadyAt={}", order.getId(), order.getRestaurantName(), estimatedReadyAt);
        eventPublisher.publish(RestaurantAcceptedEvent.now(order.getId(), order.getRestaurantName(), estimatedReadyAt));
    }
}
