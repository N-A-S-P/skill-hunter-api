# Skill Hunter API

![Skill Hunter Logo](src/main/resources/static/leopard-colored.svg) 

![Build](https://github.com/N-A-S-P/skill-hunter-api/actions/workflows/maven-tests.yml/badge.svg)
![Coverage](https://img.shields.io/badge/coverage-96%25-34D058)
![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-green)
![License](https://img.shields.io/badge/License-Apache_2.0-blue)

Skill Hunter is a personal project designed to help organize and manage the job search process. The application started life as an Access database, briefly evolved into a Django application, and is currently being rebuilt as a Java Spring Boot REST API.

The long-term goal is to provide a centralized place to track:

* Companies
* Contacts
* Positions
* Applications
* Application history
* Work history
* Professional accomplishments
* Notes and interactions

## Technology Stack

* Java 21
* Spring Boot
* Spring Data JPA
* PostgreSQL
* H2 Database
* Maven
* OpenAPI / Swagger
* JUnit 5
* Mockito
* AssertJ

## Current Status

### Implemented:

* Keycloak Authentication with Spring Boot Security
* Profile GET
* Company and Address CRUD
* Validation
* Global exception handling
* OpenAPI documentation
* Unit tests
* GitHub Actions CI

### In Progress:

* Contact management

## Running the Application

```bash
mvn spring-boot:run
```

Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

## License

Apache 2.0

## Version History
| Version # | Status | Description                                   |
|-----------|--------|-----------------------------------------------|
| 0.1.0     | Done   | Initial project setup with Profile management |
| 0.2.0     | Done   | Company and Address management                |
| 0.3.0     | Done   | Keycloak authentication and Profile endpoint  |

## Roadmap

* Contact management
* Company-Contact relationship management
* Position management
* Application tracking
* Application status history
* Interaction tracking
* Resume generation (future goal)