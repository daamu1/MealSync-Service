# MealSync

Spring Boot modular monolith for a food delivery flow using Kafka as the internal event backbone.

## Flow

1. `POST /api/orders` creates an order.
2. `OrderService` stores the order and publishes `OrderCreatedEvent`.
3. `RestaurantOrderListener` consumes the event, accepts the order, and publishes `RestaurantAcceptedEvent`.
4. `DeliveryListener` assigns a rider and publishes `DeliveryAssignedEvent`.
5. `NotificationListener` records a notification log.

## Run Locally

Start infrastructure:

```bash
docker compose up -d
```

The application imports `.env` automatically through Spring config. Update `.env` if your local Kafka or database ports, username, or password are different.

Run the application:

```bash
./gradlew bootRun
```

Swagger UI:

- http://localhost:8080/swagger-ui.html
- http://localhost:8080/v3/api-docs

Logs:

- Logs are printed to the console only.
- In IntelliJ, see logs in the Run/Debug tool window.
- In terminal, see logs where `./gradlew bootRun` is running.
- Log formatting is configured in `src/main/resources/logback-spring.xml`.
- Log levels are controlled from `.env`.

Create an order:

```bash
curl -i -X POST http://localhost:8080/api/orders \
  -H 'Content-Type: application/json' \
  -d '{"customerName":"Asha","restaurantName":"Tandoori House","totalAmount":24.50}'
```

Fetch the order with the `id` returned by the create call:

```bash
curl http://localhost:8080/api/orders/{id}
```

## Verify

```bash
./gradlew test
```
