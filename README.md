# Players Management System

A comprehensive Spring Boot-based application for managing player information, including CSV file uploads, PostgreSQL database integration, and Redis caching using the LRU (Least Recently Used) eviction policy. The entire application is containerized using Docker and Docker Compose for seamless deployment and development.

---

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
    - [Clone the Repository](#1-clone-the-repository)
    - [Configure Environment Variables](#2-configure-environment-variables)
    - [Build the Application](#3-build-the-application)
    - [Set Up Docker Containers](#4-set-up-docker-containers)
- [Usage](#usage)
    - [API Endpoints](#api-endpoints)
    - [Sample Requests](#sample-requests)
- [Testing](#testing)
- [Project Structure](#project-structure)
- [Configuration Details](#configuration-details)
    - [Application Properties](#application-properties)
    - [Redis Configuration](#redis-configuration)
- [Future Enhancements](#future-enhancements)

---

## Features

- **Player Management**: Create, read, update, and delete player information.
- **CSV File Upload**: Bulk upload player data via CSV files.
- **Database Integration**: Persistent storage using PostgreSQL.
- **Caching Mechanism**: Improved performance with Redis caching implementing LRU eviction policy for the 5 most recently queried players.
- **Dockerized Deployment**: Easy setup and deployment using Docker and Docker Compose.
- **Robust Testing**: Integration and unit tests to ensure application reliability.

---

## Technology Stack

- **Backend**:
    - Java 17
    - Spring Boot
    - Spring Data JPA
    - Spring Web
    - Spring Cache (with Redis)
- **Database**:
    - PostgreSQL
- **Caching**:
    - Redis
- **Build Tool**:
    - Maven
- **Containerization**:
    - Docker
    - Docker Compose
- **Testing**:
    - JUnit 5
    - Mockito

---

## Prerequisites

Ensure you have the following installed on your local development machine:

- **Java Development Kit (JDK) 17**
- **Maven 3.6+**
- **Docker**
- **Docker Compose**

---

## Getting Started

Follow these steps to set up and run the application locally.

### 1. Clone the Repository

```bash
https://github.com/komalkishorvohra/players-app.git
cd players-management-system
```

### 2. Configure Environment Variables

Create an `.env` file in the root directory to manage environment variables:

```dotenv
POSTGRES_USER=test_user
POSTGRES_PASSWORD=password123
POSTGRES_DB=players_db
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/players_db
SPRING_DATASOURCE_USERNAME=test_user
SPRING_DATASOURCE_PASSWORD=password123
REDIS_HOST=redis
REDIS_PORT=6379
```

### 3. Build the Application
Use Maven to build the application:
```
mvn clean install
```

### 4. Set Up Docker Containers
Start all services using Docker Compose:
```
docker-compose up -d
```


## Usage

Once all services are up and running, the application will be accessible at:
http://localhost:8080


### API Endpoints

#### Player Endpoints

| Method | Endpoint                     | Description                          |
| ------ | ---------------------------- | ------------------------------------ |
| GET    | `/api/players`               | Retrieve all players                 |
| GET    | `/api/players/{id}`          | Retrieve a player by ID (cached)     |
| POST   | `/api/players`               | Create a new player                  |
| PUT    | `/api/players/{id}`          | Update an existing player            |
| DELETE | `/api/players/{id}`          | Delete a player                      |
| POST   | `/api/players/upload-csv`    | Upload players via CSV file          |
| GET    | `/api/players/top?limit={n}` | Get top N players by ranking (cached) |

### Sample Requests

#### 1. Get All Players

**Request**
```http
GET /api/players HTTP/1.1
Host: localhost:8080
```
**Response**
```[
  {
    "id": "uuid-1",
    "name": "John Doe",
    "email": "john.doe@example.com",
    "dateOfBirth": "1990-05-15",
    "gamesPlayed": ["Chess", "Poker"],
    "ranking": 1200
  }
]
```

## Testing
### Running Tests

Execute unit and integration tests using Maven:

```bash
mvn test
```

Test Coverage
You can generate a test coverage report using the Maven JaCoCo plugin:

```bash
mvn jacoco:report
```

## Configuration Details
### Application Properties

**src/main/resources/application.properties**
```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Redis Configuration
spring.cache.type=redis
spring.redis.host=${REDIS_HOST}
spring.redis.port=${REDIS_PORT}
spring.cache.redis.time-to-live=600000  # 10 minutes in milliseconds
```

## Redis Configuration
docker/redis.conf
```
maxmemory 10mb
maxmemory-policy allkeys-lru
```

## Future Enhancements
- **Authentication and Authorization**: Implement security using Spring Security and JWT.
- **API Documentation**: Integrate Swagger for API documentation.
- **CI/CD Pipeline**: Set up a CI/CD pipeline using Jenkins or GitHub Actions.
- **Monitoring**: Add monitoring with Prometheus and Grafana.
