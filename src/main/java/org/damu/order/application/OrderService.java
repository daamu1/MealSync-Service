package org.damu.order.application;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.damu.common.events.OrderCreatedEvent;
import org.damu.kafka.MealSyncEventPublisher;
import org.damu.order.api.OrderResponse;
import org.damu.order.api.PlaceOrderRequest;
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
public class OrderService {
    private final OrderRepository orderRepository;
    private final MealSyncEventPublisher eventPublisher;

    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        Order order = Order.create(request.customerName(), request.restaurantName(), request.totalAmount());
        Order savedOrder = orderRepository.save(order);
        log.info("Order created id={} restaurant={} amount={}", savedOrder.getId(), savedOrder.getRestaurantName(), savedOrder.getTotalAmount());
        eventPublisher.publish(OrderCreatedEvent.now(savedOrder.getId(), savedOrder.getCustomerName(), savedOrder.getRestaurantName(), savedOrder.getTotalAmount()));
        return OrderResponse.from(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(UUID orderId) {
        return orderRepository.findById(orderId).map(OrderResponse::from).orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
