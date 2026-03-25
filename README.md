# Patient Management Microservices

A Java/Spring Boot microservices project for patient onboarding and management with:

- JWT-based authentication (`auth-service`)
- API gateway with JWT validation (`api-gateway-2`)
- Patient CRUD (`patient-service`)
- Billing account creation via gRPC (`billing-service`)
- Event publishing/consuming via Kafka (`patient-service` -> `analytics-service`)

## Services

| Service | Port | Purpose |
|---|---:|---|
| `api-gateway-2` | `4004` | Entry point for auth + patient APIs |
| `auth-service` | `4005` | Login + token validation |
| `patient-service` | `4000` | Patient CRUD + gRPC/Kafka integration |
| `billing-service` | `4001` (HTTP), `9001` (gRPC) | Billing gRPC server |
| `analytics-service` | `4002` | Consumes patient events from Kafka |
| `auth-service-db` (Postgres) | `5001` | DB for auth service |
| `patient-service-db` (MySQL) | `33306` | DB for patient service |
| `kafka` | `9092`, `9094` | Messaging broker |

## High-Level Flow

1. Client logs in through gateway: `POST /auth/login`.
2. Gateway routes to `auth-service` and returns JWT.
3. Client calls patient APIs with `Authorization: Bearer <token>`.
4. Gateway validates token by calling `auth-service /validate`.
5. `patient-service` handles CRUD, calls `billing-service` over gRPC on create, and publishes Kafka event.
6. `analytics-service` consumes patient events from Kafka topic `patient`.

## Run With Docker Compose

From project root:

```bash
docker compose up --build
```

Stop:

```bash
docker compose down
```

Stop and remove volumes:

```bash
docker compose down -v
```

## API Usage

Base URL:

```text
http://localhost:4004
```

### 1. Login

```bash
curl -X POST http://localhost:4004/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testuser@test.com",
    "password": "password123"
  }'
```

Response:

```json
{
  "token": "<jwt>"
}
```

### 2. Get Patients

```bash
curl http://localhost:4004/api/patients \
  -H "Authorization: Bearer <jwt>"
```

### 3. Create Patient

```bash
curl -X POST http://localhost:4004/api/patients \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <jwt>" \
  -d '{
    "name": "Anurag Rajput",
    "email": "anurag@example.com",
    "address": "Bengaluru, India",
    "dateOfBirth": "2000-01-01",
    "registeredDate": "2026-03-26"
  }'
```

## Project Structure

```text
patient-management/
├── api-gateway-2/
├── auth-service/
├── patient-service/
├── billing-service/
├── analytics-service/
├── integration-tests/
└── docker-compose.yml
```

## Notes

- `api-gateway` is intentionally not used; `api-gateway-2` is the active gateway.
- Auth seed user is initialized via `auth-service/src/main/resources/data.sql`.
- If port conflicts happen, stop local services on those ports before starting compose.
