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
✅ Customer Identity Management (Personal information, identification documents, verification)  
✅ KYC (Know Your Customer) Management  
✅ Customer Onboarding & Profile Management  
✅ Customer Status Lifecycle (Active, Suspended, Closed)  

### 🏦 Account Management
✅ Multiple Account Types Support:
- Savings Account
- Current Account
- Fixed Deposit Account
- Loan Account (Future Extension)

✅ Account Lifecycle Management:
- Account Opening
- Account Activation
- Account Blocking
- Account Closing

✅ Account Number Generation & Validation  
✅ Multi-Currency Account Support  

### 💱 Currency Management
✅ Currency Configuration Management  
✅ Support Multiple Currencies:
- USD (US Dollar)
- KHR (Cambodian Riel)
- THB (Thai Baht)
- Other International Currencies

✅ Currency-Based Account Balance Management  
✅ Exchange Rate Management 

### 💳 Transaction Management
✅ Deposit Processing  
✅ Withdrawal Processing  
✅ Internal Fund Transfer  
✅ Transaction Validation & Processing Rules  
✅ Transaction History & Statement Management  
✅ Transaction Status Tracking  

### 🔐 Security & Access Control
✅ Secure Authentication & Authorization  
✅ Role-Based Access Control (RBAC)  
✅ User Identity Management  
✅ Permission-Based API Access  
✅ Audit Logging & Activity Tracking  

### 📚 API & Documentation
✅ REST API Documentation (Swagger/OpenAPI)  
✅ API Request/Response Validation  
✅ Standard Error Handling  

### 🧪 Testing & Quality
✅ Unit Testing (JUnit 5)  
✅ Integration Testing (Spring Test)  
✅ Repository & Service Layer Testing  
✅ Banking Business Rule Testing  

### 📊 Reporting
✅ Account Balance Reports  
✅ Transaction Reports  
✅ Customer Activity Reports  
✅ Financial Summary Reports

---

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
