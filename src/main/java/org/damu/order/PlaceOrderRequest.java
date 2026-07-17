package org.damu.order;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PlaceOrderRequest(
		@NotBlank @Size(max = 120) String customerName,
		@NotBlank @Size(max = 120) String restaurantName,
		@NotNull @DecimalMin(value = "0.01") BigDecimal totalAmount
) {
}
