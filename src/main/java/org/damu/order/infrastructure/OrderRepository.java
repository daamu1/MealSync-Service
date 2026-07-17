package org.damu.order.infrastructure;

import java.util.UUID;

import org.damu.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
