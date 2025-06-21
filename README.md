# Fitness AI Microservices Platform

A comprehensive microservices-based fitness tracking platform with AI-powered recommendations, built using Spring Boot, React, and modern cloud-native technologies.

## 🚀 Features

- **Microservices Architecture**: Scalable, loosely coupled services for different domains
- **AI-Powered Recommendations**: Intelligent activity suggestions using Gemini AI
- **Real-time Communication**: Event-driven architecture with RabbitMQ
- **Service Discovery**: Netflix Eureka for service registration and discovery
- **Centralized Configuration**: Spring Cloud Config Server for configuration management
- **Modern Frontend**: React-based UI with Material-UI components
- **Authentication & Authorization**: Keycloak integration for secure access
- **Multi-Database Support**: PostgreSQL for relational data, MongoDB for document storage

## 🏗️ Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   React Frontend│    │   API Gateway   │    │   Eureka Server │
│   (Port 5173)   │◄──►│   (Port 8080)   │◄──►│   (Port 8761)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                                ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  Config Server  │    │  User Service   │    │ Activity Service│
│   (Port 8888)   │    │   (Port 8081)   │    │   (Port 8082)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │                       │
                                ▼                       ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │   PostgreSQL    │    │     MongoDB     │
                       │   (Port 5432)   │    │   (Port 27017)  │
                       └─────────────────┘    └─────────────────┘
                                │                       │
                                ▼                       ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │   AI Service    │    │    RabbitMQ     │
                       │   (Port 8083)   │    │   (Port 5672)   │
                       └─────────────────┘    └─────────────────┘
```

## 🛠️ Technology Stack

### Backend Services

- **Java 21** - Core programming language
- **Spring Boot 3.4.4** - Application framework
- **Spring Cloud 2024.0.1** - Microservices framework
- **Spring Data MongoDB** - NoSQL database integration
- **Spring Data JPA** - Relational database integration
- **Spring AMQP** - RabbitMQ integration
- **Netflix Eureka** - Service discovery
- **Spring Cloud Config** - Configuration management
- **WebFlux** - Reactive programming support
- **Lombok** - Boilerplate code reduction

### Frontend

- **React 19.1.0** - UI framework
- **Vite 6.3.5** - Build tool and dev server
- **Material-UI 7.1.1** - Component library
- **Redux Toolkit 2.8.2** - State management
- **React Router 7.6.2** - Client-side routing
- **Axios 1.10.0** - HTTP client

### Infrastructure

- **PostgreSQL** - Primary relational database
- **MongoDB** - Document database for activities
- **RabbitMQ** - Message broker
- **Keycloak** - Identity and access management

## 📁 Project Structure

```
fitness-microservice/
├── activityservice/          # Activity management microservice
├── aiservice/               # AI recommendation service
├── configserver/            # Centralized configuration server
├── eureka/                  # Service discovery server
├── fitness-Ai-frontend/     # React frontend application
├── gateway/                 # API Gateway with authentication
└── userservice/             # User management microservice
```

## 🚀 Quick Start

### Prerequisites

- Java 21 or higher
- Node.js 18+ and npm
- Docker (optional, for containerized deployment)
- PostgreSQL
- MongoDB
- RabbitMQ

### Installation

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd fitness-microservice
   ```

2. **Start Infrastructure Services**

   **PostgreSQL:**

   ```bash
   # Using Docker
   docker run --name postgres-fitness -e POSTGRES_PASSWORD=password -e POSTGRES_DB=fitness_db -p 5432:5432 -d postgres:latest

   # Or install locally
   brew install postgresql
   brew services start postgresql
   ```

   **MongoDB:**

   ```bash
   # Using Docker
   docker run --name mongodb-fitness -p 27017:27017 -d mongo:latest

   # Or install locally
   brew install mongodb-community
   brew services start mongodb-community
   ```

   **RabbitMQ:**

   ```bash
   # Using Docker
   docker run --name rabbitmq-fitness -p 5672:5672 -p 15672:15672 -d rabbitmq:management

   # Or install locally
   brew install rabbitmq
   brew services start rabbitmq
   ```

3. **Start Backend Services**

   Start services in the following order:

   ```bash
   # 1. Config Server
   cd configserver
   mvn spring-boot:run

   # 2. Eureka Server (in new terminal)
   cd ../eureka
   mvn spring-boot:run

   # 3. User Service (in new terminal)
   cd ../userservice
   mvn spring-boot:run

   # 4. Activity Service (in new terminal)
   cd ../activityservice
   mvn spring-boot:run

   # 5. AI Service (in new terminal)
   cd ../aiservice
   mvn spring-boot:run

   # 6. API Gateway (in new terminal)
   cd ../gateway
   mvn spring-boot:run
   ```

4. **Start Frontend**

   ```bash
   cd fitness-Ai-frontend
   npm install
   npm run dev
   ```

### Service Ports

| Service          | Port | Description              |
| ---------------- | ---- | ------------------------ |
| Eureka Server    | 8761 | Service discovery        |
| Config Server    | 8888 | Configuration management |
| API Gateway      | 8080 | Main entry point         |
| User Service     | 8081 | User management          |
| Activity Service | 8082 | Activity tracking        |
| AI Service       | 8083 | AI recommendations       |
| Frontend         | 5173 | React application        |

## 🔧 Configuration

### Environment Variables

Create `.env` files in each service directory or set environment variables:

```bash
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/fitness_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password

# MongoDB Configuration
SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/fitness

# RabbitMQ Configuration
SPRING_RABBITMQ_HOST=localhost
SPRING_RABBITMQ_PORT=5672
SPRING_RABBITMQ_USERNAME=guest
SPRING_RABBITMQ_PASSWORD=guest

# Eureka Configuration
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8761/eureka/

# Config Server Configuration
SPRING_CLOUD_CONFIG_URI=http://localhost:8888
```

### Keycloak Configuration

For authentication, configure Keycloak:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          keycloak:
            client-id: fitness-client
            client-secret: your-client-secret
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
        provider:
          keycloak:
            issuer-uri: http://localhost:8080/auth/realms/fitness
```

## 📚 API Documentation

### User Service Endpoints

- `POST /api/users/register` - Register new user
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Activity Service Endpoints

- `POST /api/activities` - Create new activity
- `GET /api/activities` - Get all activities
- `GET /api/activities/{id}` - Get activity by ID
- `PUT /api/activities/{id}` - Update activity
- `DELETE /api/activities/{id}` - Delete activity

### AI Service Endpoints

- `POST /api/recommendations` - Get AI recommendations
- `GET /api/recommendations/user/{userId}` - Get user-specific recommendations

## 🧪 Testing

### Backend Testing

```bash
# Run tests for all services
mvn test

# Run specific service tests
cd activityservice
mvn test
```

### Frontend Testing

```bash
cd fitness-Ai-frontend
npm test
```

## 🐳 Docker Deployment

### Build Docker Images

```bash
# Build all services
docker-compose build

# Or build individual services
docker build -t fitness-activityservice ./activityservice
docker build -t fitness-userservice ./userservice
docker build -t fitness-aiservice ./aiservice
docker build -t fitness-gateway ./gateway
```

### Run with Docker Compose

```bash
docker-compose up -d
```

## 📊 Monitoring

### Service Health Checks

- Eureka Dashboard: http://localhost:8761
- RabbitMQ Management: http://localhost:15672 (guest/guest)
- API Gateway: http://localhost:8080/actuator/health

### Logs

```bash
# View service logs
docker-compose logs -f [service-name]

# Or view individual service logs
cd activityservice
mvn spring-boot:run
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Follow Spring Boot best practices
- Use meaningful commit messages
- Write unit tests for new features
- Update documentation for API changes
- Follow the existing code style

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support and questions:

- Create an issue in the repository
- Check the documentation in each service directory
- Review the configuration files for setup guidance

## 🔄 Version History

- **v1.0.0** - Initial release with core microservices
- **v1.1.0** - Added AI recommendation service
- **v1.2.0** - Enhanced frontend with Material-UI
- **v1.3.0** - Added Keycloak authentication

---

**Built with ❤️ using Spring Boot and React**
