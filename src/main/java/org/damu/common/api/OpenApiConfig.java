package org.damu.common.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
		info = @Info(
				title = "MealSync API",
				version = "v1",
				description = "Modular monolith food delivery API using Kafka-backed internal events.",
				contact = @Contact(name = "MealSync Engineering"),
				license = @License(name = "Private")
		),
		servers = {
				@Server(url = "/", description = "Current server"),
				@Server(url = "http://localhost:8080", description = "Local development"),
				@Server(url = "https://api-dev.mealsync.com", description = "Development server"),
				@Server(url = "https://api.mealsync.com", description = "Production server")
		}
)
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT"
)
class OpenApiConfig {
}
