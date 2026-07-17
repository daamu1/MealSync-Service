package org.damu.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Duration;

import org.damu.delivery.DeliveryAssignmentRepository;
import org.damu.notification.NotificationLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@EmbeddedKafka(partitions = 1)
@DirtiesContext
class OrderFlowTest {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private DeliveryAssignmentRepository deliveryAssignmentRepository;

	@Autowired
	private NotificationLogRepository notificationLogRepository;

	@Autowired
	private OrderService orderService;

	@Test
	void placesOrderAndCompletesKafkaDrivenModuleFlow() throws InterruptedException {
		OrderResponse response = orderService.placeOrder(new PlaceOrderRequest(
				"Asha",
				"Tandoori House",
				new BigDecimal("24.50")
		));

		Order assignedOrder = waitForAssignedOrder(response.id());
		assertThat(assignedOrder.getStatus()).isEqualTo(OrderStatus.DELIVERY_ASSIGNED);
		assertThat(deliveryAssignmentRepository.findById(response.id())).isPresent();
		assertThat(notificationLogRepository.count()).isEqualTo(1);
	}

	private Order waitForAssignedOrder(java.util.UUID orderId) throws InterruptedException {
		long deadline = System.nanoTime() + Duration.ofSeconds(10).toNanos();
		while (System.nanoTime() < deadline) {
			Order order = orderRepository.findById(orderId).orElseThrow();
			if (order.getStatus() == OrderStatus.DELIVERY_ASSIGNED && notificationLogRepository.count() == 1) {
				return order;
			}
			Thread.sleep(100);
		}
		return orderRepository.findById(orderId).orElseThrow();
	}
}
