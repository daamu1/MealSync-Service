package org.damu.notification.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.damu.common.events.DeliveryAssignedEvent;
import org.damu.notification.domain.NotificationLog;
import org.damu.notification.infrastructure.NotificationLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationLogRepository notificationLogRepository;

    @Transactional
    public void notifyDeliveryAssigned(DeliveryAssignedEvent event) {
        String message = "Order " + event.orderId() + " assigned to " + event.riderName();
        notificationLogRepository.save(NotificationLog.create(event.orderId(), message));
        log.info("Notification recorded orderId={} rider={}", event.orderId(), event.riderName());
    }
}
