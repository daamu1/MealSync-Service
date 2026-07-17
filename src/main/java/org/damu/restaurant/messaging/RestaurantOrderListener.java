package org.damu.restaurant.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.damu.common.events.OrderCreatedEvent;
import org.damu.kafka.KafkaTopics;
import org.damu.restaurant.application.RestaurantService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RestaurantOrderListener {

    private final RestaurantService restaurantService;

    @KafkaListener(topics = KafkaTopics.ORDER_CREATED, groupId = "mealsync-restaurant")
    void onOrderCreated(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent {}", event);
        restaurantService.acceptOrder(event);
    }
}
