# Fraud Detection System

A SQL-driven fraud detection analytics system built with Java, JDBC, PostgreSQL, and a Web UI (JSP/Servlets).

## Tech Stack
- **Backend**: Java 17, JDBC, Maven
- **Database**: PostgreSQL 14+
- **Frontend**: JSP, Servlets, Vanilla CSS
- **Infrastructure**: Docker, Docker Compose, Jetty (Embedded)

## Features
- **Fraud Engine**: 6 advanced detection rules using SQL Window Functions and CTEs.
- **Real-time Dashboard**: Visual summary of alert severity (High/Medium/Low).
- **Security Logs**: Dedicated **Login History** and **Transaction** logs for audit trails.
- **Context-Rich Alerts**: Detailed descriptions explaining exactly *why* an alert was fired.
- **Zero-Setup Execution**: Automatic database schema creation and sample data seeding.

## Fraud Detection Rules
1. **Spending Spike** — amount > 3x user's 30-day rolling average.
2. **High Frequency** — 5+ transactions within 10 minutes.
3. **Impossible Travel** — same user in 2 countries within 1 hour.
4. **New Device** — transaction from device first seen within 24 hours.
5. **Night Activity** — transactions between 1AM–4AM.
6. **Failed Login Cluster** — 3+ failed logins within 5 minutes.

---

## How to Run (Recommended: Docker)

The easiest way to run the project. No need to install Java, Maven, or PostgreSQL locally.

1. **Start the system**:
   ```bash
   docker-compose up --build
   ```
2. **Access the Dashboard**:
   Go to [http://localhost:8080](http://localhost:8080)

*Note: The database is automatically initialized and seeded with sample data on the first run.*

---

## Manual Execution (Local Environment)

If you prefer to run it without Docker:

### 1. Prerequisites
- Java 17+, Maven 3.8+, PostgreSQL 14+

### 2. Configure Database
Create a database named `frauddetection` and update `src/main/resources/db.properties`:
```properties
db.url=jdbc:postgresql://localhost:5432/frauddetection
db.user=your_username
db.password=your_password
```

### 3. Initialize & Run
```bash
# Seed the database
mvn compile exec:java -Dexec.mainClass="com.frauddetection.DatabaseInitializer"

# Start the server
mvn jetty:run
```
Access at [http://localhost:8080](http://localhost:8080).

---

## Project Structure
- `src/main/java/com/frauddetection`: Core logic, DAOs, and Database configuration.
- `src/main/java/com/frauddetection/servlet`: Controllers for handling web requests.
- `src/main/webapp/WEB-INF/jsp`: UI templates (Views).
- `docker-compose.yml`: Orchestration for App and Database.