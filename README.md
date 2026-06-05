# Skill Hunter API

Skill Hunter is a personal project designed to help organize and manage the job search process.

The long-term goal is to provide a centralized place to track:

* Companies
* Contacts
* Positions
* Applications
* Application history
* Professional accomplishments
* Notes and interactions

The project began as an Access database, briefly evolved into a Django application, and is currently being rebuilt as a Java Spring Boot REST API.

## Technology Stack

* Java 17
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

* AppUser CRUD API
* Validation
* Global exception handling
* OpenAPI documentation
* Unit tests

In Progress:

* Company management
* Authentication and authorization
* Expanded test coverage

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
