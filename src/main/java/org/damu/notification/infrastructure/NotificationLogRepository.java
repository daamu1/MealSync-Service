package org.damu.notification.infrastructure;

import java.util.UUID;

import org.damu.notification.domain.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, UUID> {
}
