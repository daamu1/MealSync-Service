package org.damu.notification.domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "notification_logs")
public class NotificationLog {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(nullable = false)
	private UUID orderId;

	@Column(nullable = false, length = 500)
	private String message;

	@Column(nullable = false, updatable = false)
	private Instant sentAt;

	protected NotificationLog() {
	}

	private NotificationLog(UUID orderId, String message) {
		this.orderId = orderId;
		this.message = message;
	}

	public static NotificationLog create(UUID orderId, String message) {
		return new NotificationLog(orderId, message);
	}

	@PrePersist
	void onCreate() {
		sentAt = Instant.now();
	}
}
