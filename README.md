# Fitness Microservices Project

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Services Breakdown](#services-breakdown)
- [RabbitMQ Configuration](#rabbitmq-configuration)
- [PostgreSQL Configuration](#postgresql-configuration)
- [How to Run](#how-to-run)
- [Configuration](#configuration)
- [Contributing](#contributing)
- [License](#license)

---

## Introduction
This project is a microservices-based architecture for managing fitness activities. It provides robust features such as data auditing, user and activity management, AI-driven recommendations, and more. Each microservice is designed to handle a specific domain, such as users, activities, and API gateway for routing and authentication.

## Features
- Modular microservice architecture for scalability.
- Integration with RabbitMQ for message brokers and event-driven communication.
- PostgreSQL for structured data storage.
- MongoDB for NoSQL functionality.
- AI-based activity recommendations.
- Secure token-based authentication.

## Technologies Used
- **Java 21**: Core language for development.
- **Spring Boot**: Dependency injection, REST services, and other backend functionalities.
- **RabbitMQ**: Message broker for inter-service communication.
- **PostgreSQL**: Relational database for user & critical structured data.
- **MongoDB**: Schema-less database for activity-related documents.
- **Jakarta EE Framework**: Enterprise-ready libraries.
- **Lombok**: Reduced boilerplate with annotations.

---

## Services Breakdown

### 1. **User Service**
Located at: `userservice/`
- Handles user management such as registration, authentication, and profile handling.
- Provides APIs to manage user roles and permissions.
- Makes use of PostgreSQL for structured relational data storage.

#### Key Features:
- **User Authentication**: Integration with external tools (like Keycloak).
- **Validation Service**: Ensures all user records comply with application criteria.

---

### 2. **Activity Service**
Located at: `activityservice/`
- Manages fitness activities including creating, modifying, and auditing activities.
- Supports MongoDB for unstructured and schema-flexible data.

#### Key Features:
- **Activity AI Service**: Provides activity recommendations using AI.
- **Real-Time Event Listener**: Listens to RabbitMQ messages for updates or instructions.

---

### 3. **Gateway Service**
Located at: `gateway/`
- Serves as an API Gateway for routing requests effectively across microservices.
- Provides centralized authentication and authorization using Keycloak.

#### Key Features:
- **Middleware Authentication**: A `KeycloakUserSyncFilter` validates user tokens for secured operations.
- **User Service Proxy**: Relays requests to the User Service for any user-related operations.

---

### 4. **Recommendation Service**
Located across services where AI is applied.
- Ensures personalized suggestions for users on activities based on previous records or predefined preferences.

---

### 5. **Gemini Service**
- Placeholder for business-specific operations tied to Gemini (assumed another domain entity).

---

## RabbitMQ Configuration
RabbitMQ acts as a message broker for asynchronous communication between microservices.

1. **Installation**:
   Ensure RabbitMQ is installed and running before starting the application. You can install it using:
   ```bash
   brew install rabbitmq
   ```

2. **Configuration**:
   Update RabbitMQ properties in `application.properties` or `application.yml`:
   ```properties
   spring.rabbitmq.host=localhost
   spring.rabbitmq.port=5672
   spring.rabbitmq.username=guest
   spring.rabbitmq.password=guest
   ```

3. **Usage in Services**:
  - The `ActivityMessageListener` listens to message queues to process activity-related events.
  - Other microservices publish event-specific messages to RabbitMQ.

---

## PostgreSQL Configuration
PostgreSQL is used for handling structured, relational data. Here's how to configure it:

1. **Prerequisite**:
   Ensure PostgreSQL is installed and set up on your local machine or server.

2. **Configuration**:
   Add PostgreSQL settings in your service's `application.yml`:
   ```yaml
   spring.datasource.url=jdbc:postgresql://localhost:5432/fitness_db
   spring.datasource.username=postgres
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Database Schema**:
   All necessary schema changes are applied automatically by Hibernate (or specify schema files).

---

## How to Run

### Clone the Repository
```bash
git clone <repository-url>
cd fitness-microservices
```

### Build and Run Services
Build each service independently or use a script to automate service start-up:
```bash
# Example for Activity Service
cd activityservice
mvn clean install
mvn spring-boot:run
```

Run other services similarly, ensuring both RabbitMQ and databases are running.

---

## Contributing
We welcome contributions! To contribute:
1. Fork the repository.
2. Create a new branch for your feature.
3. Submit a pull request with a description of the changes.

---

## License
This project is licensed under the [MIT License](LICENSE).

---

Let me know if you need help filling in more specific details! You can also provide any additional code snippets or files for deeper insights.