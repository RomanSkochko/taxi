# Taxi Service API

This is the backend API for a taxi/ride-sharing service application, built with Java 21 and Spring Boot 3.4.4. It provides functionalities for user management and JWT-based authentication, featuring role-based access control and rate limiting. The project is structured modularly by features (user, auth, and planned modules like ride, driver, pricing, notification) using a standard layered architecture (controller, service, repository, DTO, entity, mapper). Planned extensions include ride handling, driver/passenger profiles, dynamic pricing calculations, and user notifications.

**Implemented Features:** User registration, login (JWT), profile view/update, password change, user deletion. Security includes role checks (USER, DRIVER) and API rate limiting via Bucket4j/Redis. Redis caching is implemented for user details to enhance performance.

**Planned Features:** Comprehensive ride management (creation, tracking, searching), driver/passenger profile specifics, dynamic pricing logic, and a notification system.

**Core Technologies:** Spring Boot (Web, Data JPA, Security, Cache), PostgreSQL (likely with PostGIS/JTS for location data), Redis (Caching & Rate Limiting), Liquibase (DB Migrations), JWT (Authentication), MapStruct (Mapping), Lombok, Bucket4j (Rate Limiting), Maven (Build), Docker & Docker Compose (Containerization), JUnit 5 & Testcontainers (Testing).

**Getting Started:**
* **Prerequisites:** JDK 21+, Maven 3+, Docker & Docker Compose.
* **Installation:** Clone the repository (`git clone <repository-url> && cd taxi`) and build (`mvn clean install -DskipTests`).
* **Running (Docker - Recommended):** Ensure Docker is running and execute `docker-compose up --build` from the project root. This starts the application (port 8080), PostgreSQL (port 5432), and Redis (port 6379).
* **Running (Local Maven):** Ensure local PostgreSQL and Redis instances are running and configured in `src/main/resources/application-local.yml`. Then run `mvn spring-boot:run -Dspring-boot.run.profiles=local`. The API will be available at `http://localhost:8080`.

**Configuration:** Key settings (database connection, Redis details, JWT secret, cache TTLs) are in `application.yml` (default/Docker) and can be overridden for local development in `application-local.yml`.

**Testing:** Run tests using `mvn test`. Integration tests leverage Testcontainers to automatically manage temporary PostgreSQL and Redis instances, ensuring isolated test environments.
