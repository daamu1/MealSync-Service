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
cp .env.example .env
docker compose up -d
```

Run the application with the default in-memory H2 database:

```bash
./gradlew bootRun
```

Or run with Postgres:

```bash
set -a
source .env
set +a
./gradlew bootRun
```

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
