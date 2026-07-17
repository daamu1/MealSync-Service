package org.damu.notification.application;

import org.damu.common.events.DeliveryAssignedEvent;
import org.damu.notification.domain.NotificationLog;
import org.damu.notification.infrastructure.NotificationLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

	private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

	private final NotificationLogRepository notificationLogRepository;

	public NotificationService(NotificationLogRepository notificationLogRepository) {
		this.notificationLogRepository = notificationLogRepository;
	}

	@Transactional
	public void notifyDeliveryAssigned(DeliveryAssignedEvent event) {
		String message = "Order " + event.orderId() + " assigned to " + event.riderName();
		notificationLogRepository.save(NotificationLog.create(event.orderId(), message));
		log.info("Notification recorded orderId={} rider={}", event.orderId(), event.riderName());
	}
}
