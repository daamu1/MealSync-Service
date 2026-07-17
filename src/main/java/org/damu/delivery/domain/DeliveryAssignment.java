package org.damu.delivery.domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "delivery_assignments")
public class DeliveryAssignment {

    @Id
    private UUID orderId;

    @Column(nullable = false, length = 120)
    private String riderName;

    @Column(nullable = false, updatable = false)
    private Instant assignedAt;

    protected DeliveryAssignment() {
    }

    private DeliveryAssignment(UUID orderId, String riderName) {
        this.orderId = orderId;
        this.riderName = riderName;
    }

    public static DeliveryAssignment create(UUID orderId, String riderName) {
        return new DeliveryAssignment(orderId, riderName);
    }

    @PrePersist
    void onCreate() {
        assignedAt = Instant.now();
    }

}
