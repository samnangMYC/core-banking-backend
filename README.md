<!-- Modern Gradient Header -->
<h1 align="center">🏦 Trendy - Core Banking System (CBS) </h1>
<h3 align="center">Scalable,Banking Platform with Spring Boot (CBS)</h3>

<p align="center">
  <a href="https://github.com/samnangMYC/core-banking-backend">
    <img src="https://img.shields.io/github/stars/samnangMYC/core-banking-backend?style=social" alt="GitHub stars" />
  </a>
  <a href="https://github.com/samnangMYC/core-banking-backend/issues">
    <img src="https://img.shields.io/github/issues/samnangMYC/core-banking-backend" alt="GitHub issues" />
  </a>
  <a href="https://github.com/yourusername/core-banking-backend/blob/main/LICENSE">
    <img src="https://img.shields.io/github/license/samnangMYC/core-banking-backend" alt="License" />
  </a>
</p>

---

## 🌟 Description

A Core Banking System (CBS) built using Spring Boot following a monolithic architecture. This system is designed for simplicity, maintainability, and enterprise-grade reliability, covering essential banking operations such as customer management, accounts, transactions, and reporting.

It is suitable for small to medium banks or fintech applications that require a single deployable backend system without microservices complexity.

Keywords for SEO: Core Banking System, Spring Boot Banking App, Monolithic Banking System, Banking Software, Digital Banking Platform, Secure Banking Backend, Open Source Banking System

---

## 🚀 Features

### 👤 Identity & Customer Management
- Customer profile management
- Customer identity document management
- KYC (Know Your Customer) verification
- Customer status lifecycle management (Active, Suspended, Closed)

### 🏦 Account Management
- Support multiple account types:
  - Savings Account
  - Current Account
  - Fixed Deposit Account
  - Loan Account (Future Extension)
- Account opening and activation
- Account blocking and closing
- Account number generation and validation
- Multi-currency account support

### 💱 Currency Management
- Currency configuration management
- Support multiple currencies:
  - USD (US Dollar)
  - KHR (Cambodian Riel)
  - THB (Thai Baht)
  - Other international currencies
- Currency-based account balance management
- Exchange rate management

### 💳 Transaction Management
- Deposit processing
- Withdrawal processing
- Internal fund transfer
- Transaction validation and business rules
- Transaction history and account statements
- Transaction status tracking

### 📚 Ledger Management
- Double-entry ledger recording
- Debit and credit transaction tracking
- Transaction reference and audit history
- Running balance management

### 🔐 Security & Access Control
- Secure authentication and authorization
- Role-Based Access Control (RBAC)
- User identity management
- Permission-based API access
- Audit logging and activity tracking

### 📚 API & Documentation
- RESTful API design
- Swagger/OpenAPI documentation
- Request and response validation
- Standard error handling

### 🧪 Testing & Quality
- Unit testing with JUnit 5
- Integration testing with Spring Test
- Repository and service layer testing
- Banking business rule testing
  
## 💻 Tech Stack


| Layer | Technology |
|----------------------|----------------------------------------------|
| Backend | Java 25, Spring Boot 4.1.0 |
| Architecture | Monolithic Architecture, Layered Architecture |
| Data Access | Spring Data JPA, Hibernate ORM |
| Security & Identity | Keycloak, Spring Security, OAuth2 / OpenID Connect |
| Database | PostgreSQL |
| Cache & Performance | Redis |
| API | RESTful API, OpenAPI / Swagger |
| Build Tool | Maven |
| Containerization | Docker |
| Testing | JUnit 5, Mockito, Spring Boot Test, Testcontainers |
| Database Migration | Flyway / Liquibase |
| Documentation | Swagger/OpenAPI, Markdown, Mermaid Diagram| Monitoring | Prometheus, Grafana, |
| Logging | SLF4J, Logback |
| Version Control | Git, GitHub |

---

## 📂 Project Structure
```text
trendy-core-banking-system/
├── README.md
├── pom.xml
├── docker-compose.yml
│
├── src/
│   ├── main/java/com/trendy/cbs/
│   │
│   │   ├── audit/              # Audit logging & compliance tracking
│   │   │
│   │   ├── config/             # Application configurations
│   │   │                       # Security, Database, Bean configurations
│   │   │
│   │   ├── controller/         # REST API Controllers
│   │   │                       # Handles HTTP requests and responses
│   │   │
│   │   ├── entity/             # JPA Database Entities
│   │   │                       # Customer, Account, Transaction models
│   │   │
│   │   ├── enums/              # System Enumerations
│   │   │                       # Account Type, Currency, Status, Transaction Type
│   │   │
│   │   ├── exception/          # Global Exception Handling
│   │   │                       # Business exceptions & API error responses
│   │   │
│   │   ├── helper/             # Common Utility Components
│   │   │                       # Shared helper functions
│   │   │
│   │   ├── init/               # Application Initialization
│   │   │                       # Default data loading & startup process
│   │   │
│   │   ├── mapper/             # Entity and DTO Mapping
│   │   │                       # Converts Entity ↔ DTO objects
│   │   │
│   │   ├── payload/            # API Data Transfer Objects
│   │   │                       # Request & Response models
│   │   │
│   │   ├── repos/              # Data Repository Layer
│   │   │                       # Spring Data JPA database operations
│   │   │
│   │   ├── security/           # Security & Identity Management
│   │   │                       # Authentication, Authorization, RBAC
│   │   │
│   │   └── service/            # Business Logic Layer
│   │                               # Banking workflows and rules
│   │
│   └── main/resources/
│       │
│       ├── application.yml     # Application configuration
│       │
│       ├── db/
│       │   └── migration/      # Database migration scripts
│       │                       # Flyway / Liquibase
│       │
│       └── logback.xml         # Logging configuration
│
└── scripts/                    # DevOps & Automation Scripts
    ├── deployment/
    ├── database/
    └── CI/CD pipelines
```
### Package Responsibilities

| Package | Responsibility |
|---------|----------------|
| `controller` | Exposes REST APIs for banking operations |
| `service` | Implements core banking business rules |
| `entity` | Defines database domain models |
| `repos` | Handles database access |
| `payload` | API request and response contracts |
| `mapper` | Converts between DTOs and entities |
| `security` | Authentication, authorization, and identity management |
| `audit` | Records system activities and compliance logs |
| `config` | Application and infrastructure configuration |
| `exception` | Centralized error handling |
| `enums` | Banking constants and status definitions |
| `init` | Startup initialization logic |
| `helper` | Reusable utility components |
