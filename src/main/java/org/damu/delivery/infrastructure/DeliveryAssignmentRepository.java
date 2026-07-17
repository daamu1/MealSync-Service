package org.damu.delivery.infrastructure;

import java.util.UUID;

import org.damu.delivery.domain.DeliveryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment, UUID> {
}
