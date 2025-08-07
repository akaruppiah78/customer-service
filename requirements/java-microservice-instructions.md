
# 🧾 Java Spring Boot Microservice Development Guidelines (MongoDB + OpenJDK 21)

## 📁 Project Overview

Create a **Customer Management Microservice** using:

* **Java 21 (OpenJDK)** — latest LTS
* **Spring Boot 3.3+**
* **MongoDB** (running locally)
* RESTful APIs
* OpenAPI documentation
* Layered architecture with best practices
* GitHub Copilot Agent friendly structure

---

## 🧰 Environment Setup

- Ensure **OpenJDK 21** is installed.
- If not available, the agent must automatically install OpenJDK 21 using system-specific tools (e.g., Homebrew, SDKMAN).
- All dependencies and libraries used **must be compatible** with Java 21.

---

## 📂 Recommended Project Structure

```
customer-service/
├── src/
│   ├── main/
│   │   ├── java/com/example/customerservice/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── model/
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   ├── config/
│   │   │   └── CustomerServiceApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── static/
│   └── test/
│       └── java/com/example/customerservice/
├── .gitignore
├── Dockerfile (optional)
├── pom.xml
└── README.md
```

---

## 🔧 Technology Stack

| Component  | Library/Tool                      | Version (as of Aug 2025) |
| ---------- | --------------------------------- | ------------------------ |
| Java       | OpenJDK                           | 21                       |
| Framework  | Spring Boot                       | 3.3.x                    |
| MongoDB    | Spring Data MongoDB               | Starter with Spring Boot |
| API Docs   | springdoc-openapi                 | 2.5.x                    |
| Validation | Jakarta Bean Validation (JSR-380) | 3.0+                     |
| Logging    | Logback / SLF4J                   | Default in Spring Boot   |
| Testing    | JUnit 5, Mockito                  | Latest                   |
| Mapping    | MapStruct                         | 1.5.x                    |
| Build Tool | Maven                             | 3.9+                     |

---

## ✅ Coding Standards & Best Practices

- Follow secure coding guidelines:
  - Avoid hardcoding secrets
  - Sanitize user input
  - Use input validation and output encoding
  - Prevent NoSQL injections
- Use **Java 21** features: `record`, `sealed`, `pattern matching`
- Minimize use of Lombok, favor immutability and POJOs
- Follow layered architecture:
  - DTO ↔ Service ↔ Repository
- Validate all inputs using `@Valid`, `@Pattern`, `@Email`, etc.
- Use centralized error handling
- Use constructor-based dependency injection only

---

## 🧪 Unit Testing Guidelines

- Use **JUnit 5** and **Mockito**
- Test by layers:
  - `@WebMvcTest` for controller
  - `@DataMongoTest` for repository with embedded Mongo
- Aim for 80%+ test coverage
- Follow AAA: Arrange → Act → Assert
- Test both success and failure paths

---

## ⚠️ Error Handling Design

Use centralized `@ControllerAdvice` for global error mapping.

Define custom exceptions and map them to HTTP status codes using structured JSON responses.

---

## 🔁 API Versioning and Compatibility

- All REST endpoints **must be versioned** using `/api/v1/...`
- Ensure **backward compatibility** with older versions when adding new features
- Maintain separate controllers if needed for future versions (`CustomerControllerV2`, etc.)

---

## 📦 Standard API Response Format

```json
{
  "status": "SUCCESS",
  "message": "Customer created successfully",
  "data": {
    "customerId": "abc-123"
  },
  "timestamp": "2025-08-04T14:00:00Z"
}
```

Define a reusable `ApiResponse<T>` record.

---

## 🛠️ MongoDB Configuration

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/customerdb
  jackson:
    serialization:
      WRITE_DATES_AS_TIMESTAMPS: false
```

---

## 🔄 CRUD REST Endpoints (Versioned)

| Method | URL                          | Description             |
|--------|------------------------------|-------------------------|
| POST   | `/api/v1/customers`          | Create new customer     |
| GET    | `/api/v1/customers/{id}`     | Get customer by ID      |
| PUT    | `/api/v1/customers/{id}`     | Update customer         |
| DELETE | `/api/v1/customers/{id}`     | Delete customer         |
| GET    | `/api/v1/customers/search`   | Filter/search customers |

---

## 📘 OpenAPI Integration

```xml
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.5.0</version>
</dependency>
```

Swagger UI available at `/swagger-ui.html`

---

## 🧼 Clean Code Automation

- Format with **Prettier** / **Spotless**
- Lint with **Checkstyle** or **PMD**
- Scan with **SonarLint** locally
- Use **Maven Enforcer** plugin to pin Java version

---

## 🐳 Optional Docker Setup

- Define a `Dockerfile` for the Spring Boot service
- Use `docker-compose.yml` to spin up MongoDB locally

---

## 📦 Artifacts and Workspace

- All instruction files and OpenAPI spec reside in the `requirements/` folder
- Use the MongoDB MCP for collection creation and validation
