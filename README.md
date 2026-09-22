# Notification
The **Notification Microservice** dispatches emails triggered by certain events in the Classroom Application — a distributed, event-driven system built with a microservices architecture (see full [technical documentation](#) for details).

The entire Classrooms Application is deployed and available at [www.book-your-classroom.com](https://www.book-your-classroom.com).

## Table of Contents
- [Purpose](#purpose)
- [Tech Stack](#tech-stack)
- [Tests](#tests)
- [Contribution and License](#contribution-and-license)
- [Contact](#contact)

## Purpose

The **Notification Microservice** is an event-driven worker designed to dispatch real-time email notifications triggered by events across the Classroom Application ecosystem (e.g., booking confirmations, watch alerts). At present, **email is the only supported notification channel**.

### Architectural Scope & Deployment Philosophy

Unlike other microservices in the Classroom Application, this service **does not offer an isolated or standalone local deployment setup**. 

Due to its nature, a standalone setup is deliberately omitted for the following reasons:
* **Event-Driven & Asynchronous:** It exposes no HTTP REST endpoints and operates purely as a Kafka consumer. Manual testing via simulated Kafka payloads is cumbersome and does not accurately reflect true system behavior.
* **Ecosystem-Dependent:** Its primary value lies in its reactive integration with domain event producers (such as the *User* and *Booking* microservices). It is intended to be tested and run strictly within the unified, platform-wide Docker Compose environment.
* **Credential Security:** Running email deliveries in isolated environments risks exposing sensitive SMTP credentials. Keeping the service bound to the unified deployment ensures credentials remain securely injected via protected environment variables.


## Tech Stack

### Core Technologies
* **Java 17**
* **Spring Boot 3.3.0**
* **Build Tool:** Maven

### Messaging & Communication
* **Apache Kafka & Spring Kafka** 
* **Spring Boot Starter Mail:** Email dispatching service via SMTP.

### Persistence & Data
* **Spring Data JPA** 
* **MySQL** 

### Dependencies & Shared Libraries
* **Classroom Shared Library:** Domain and shared transfer objects (`com.github.jcasaslopez:classroom-shared-library:0.0.7`).
* **Jackson (Spring JSON)** 

### Testing Framework
* **JUnit 5**
* **Mockito** 
* **Testcontainers (MySQL & Kafka)** 
* **GreenMail (`greenmail-junit5`):** In-memory SMTP server for integration testing of email delivery.
* **Spring Kafka Test:** Utilities for embedded Kafka testing.

  
## Tests

The microservice includes a focused integration test suite using **Testcontainers** (for isolated Kafka and MySQL instances) and **GreenMail** (for in-memory SMTP mocking) to validate the email dispatching workflows without external dependencies.

### Prerequisites
* **Java 17** or higher.
* **Docker Desktop / Engine** running (required by Testcontainers).

### Running Tests

```bash
# 1. Clone the repository
git clone https://github.com/JCasasLopez/classroom-notification-service

# 2. Navigate to the project directory
cd classroom-notification-service

# 3. Run tests
mvn clean test
```


## Contribution and License
### Contributing
As this project is intended as a personal demo, external contributions are not being accepted at this time.

### License
This project is licensed under the MIT License.  
See the [LICENSE](./LICENSE) file for details.

## Contact
Created by Jorge Casas López.  
Email: [j.casas.lopez.26@gmail.com](mailto:j.casas.lopez.26@gmail.com) 
