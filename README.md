# Siemens Java Internship – Refactoring Project

This is a Spring Boot CRUD application implemented as part of the Siemens internship challenge.  
The goal was to refactor and improve a basic application while ensuring code quality, validation, proper status codes, and testing.

---

## ✅ Features

- Full CRUD operations for `Item` entities
- Validation using `@Valid`, `@NotBlank`, `@Email`
- Asynchronous processing with `CompletableFuture`
- Thread-safe logic and exception handling
- Proper HTTP status codes (200, 201, 400, 404, 204)
- Unit, integration and performance tests with JUnit 5
- In-memory H2 database (no setup required)
- Manual testing via Postman (screenshots available)

---

## 🛠 Technologies Used

- Java 17
- Spring Boot
- Spring Web + Validation
- Spring Data JPA
- H2 Database
- JUnit 5
- Postman

---

## 🚀 How to Run

```bash
mvn spring-boot:run
