# Skill Hunter API

![Build](https://github.com/N-A-S-P/skill-hunter-api/actions/workflows/maven-tests.yml/badge.svg)
![Coverage](https://img.shields.io/badge/coverage-92%25-34D058)
![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0-green)
![License](https://img.shields.io/badge/License-Apache_2.0-blue)

Skill Hunter is a personal project designed to help organize and manage the job search process.

The long-term goal is to provide a centralized place to track:

* Companies
* Contacts
* Positions
* Applications
* Application history
* Work history
* Professional accomplishments
* Notes and interactions

The project began as an Access database, briefly evolved into a Django application, and is currently being rebuilt as a Java Spring Boot REST API.

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

Implemented:

* Authentication
* Profile GET
* Company and Address CRUD
* Validation
* Global exception handling
* OpenAPI documentation
* Unit tests

In Progress:

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
