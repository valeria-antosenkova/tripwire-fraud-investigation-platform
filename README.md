# Tripwire — Refund Fraud Detection Platform

Tripwire is a Spring Boot web application that helps fraud analysts review, investigate, and resolve refund cases by combining automated risk scoring with a clean case-management interface.

---

## Features

- **Fraud risk scoring** — accounts are scored automatically using the Dymo API and local heuristics (disposable email domains, phone/IBAN country mismatch, account age)
- **Case management** — analysts can browse, filter, and action transactions with statuses: `UNASSIGNED`, `UNDER_REVIEW`, `APPROVED`, `DENIED`
- **Role-based access** — `ADMIN` and `ANALYST` roles with account management for admins
- **Refund case detail view** — full transaction breakdown including order items, payment method, shipping/billing addresses, and agent notes
- **UI** — single-page interface served directly from the Spring Boot app

---

## Screenshots

### Dashboard
![Dashboard](src/main/demo%20pics/Screenshot%202026-06-09%20at%2011.08.19%20AM.png)

### Case List
![Case List](src/main/demo%20pics/Screenshot%202026-06-09%20at%2011.08.33%20AM.png)

### Refund Case Detail
![Refund Case Detail](src/main/demo%20pics/Screenshot%202026-06-09%20at%2011.09.05%20AM.png)

### Account / Risk Profile
![Account Risk Profile](src/main/demo%20pics/Screenshot%202026-06-09%20at%2011.09.12%20AM.png)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot, Spring Security Crypto |
| Database | H2 (in-memory), Spring JDBC |
| Frontend | Vanilla HTML/CSS/JS served as a static resource |
| Build | Maven |
| External API | Dymo fraud-intelligence API (optional) |

---

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+



## Project Structure

```
src/main/java/com/teamcrocodile/tripwire/
├── controller/     # REST endpoints (Auth, Agent, Account, Transaction, Refund)
├── dao/            # Data access layer (JDBC)
├── model/          # Domain models (Account, Agent, Transaction, Status, AgentRole)
├── service/        # Business logic (DymoService for fraud scoring)
└── view/           # Console I/O utilities

src/main/resources/
├── schema.sql      # Database schema
├── data.sql        # Seed data
└── static/
    └── index.html  # Single-page frontend
```



