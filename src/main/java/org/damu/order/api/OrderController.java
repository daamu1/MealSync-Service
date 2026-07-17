package org.damu.order.api;

import java.net.URI;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.damu.order.application.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Create and inspect food delivery orders")
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	@Operation(summary = "Place an order", description = "Creates an order and publishes the OrderCreatedEvent to Kafka.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Order created"),
			@ApiResponse(responseCode = "400", description = "Invalid order request")
	})
	public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
		OrderResponse response = orderService.placeOrder(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(response.id())
				.toUri();
		return ResponseEntity.created(location).body(response);
	}

	@GetMapping("/{orderId}")
	@Operation(summary = "Get an order", description = "Returns the current persisted order state.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Order found"),
			@ApiResponse(responseCode = "404", description = "Order not found")
	})
	public OrderResponse getOrder(@PathVariable UUID orderId) {
		return orderService.getOrder(orderId);
	}
}
