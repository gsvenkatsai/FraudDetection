# Fraud Detection System

A SQL-driven fraud detection analytics system built with Java, JDBC, PostgreSQL, and Swing UI.

## Tech Stack
- Java 17
- PostgreSQL
- JDBC
- Maven
- Swing UI

## Features
- 6 fraud detection rules using Window Functions and CTEs
- Real-time alert dashboard with color-coded severity levels (High/Medium/Low)
- Context-rich alert descriptions (e.g. detailed calculations of spending averages or exact travel durations)
- Rich User details including name mapping from the database
- DAO pattern for clean separation of concerns

## Fraud Detection Rules
1. Spending Spike — amount > 3x user's 30-day rolling average
2. High Frequency — 5+ transactions within 10 minutes
3. Impossible Travel — same user in 2 countries within 1 hour
4. New Device — transaction from device first seen within 24 hours
5. Night Activity — transactions between 1AM–4AM
6. Failed Login Cluster — 3+ failed logins within 5 minutes

## Setup
1. Create PostgreSQL database: `frauddetection`
2. Update `src/main/resources/db.properties` with your credentials
3. Run `mvn compile`
4. Launch `FraudDetectionApp` and click "Initialize/Reset Database"
5. Click "Run Fraud Detection Rules"

## Prerequisites
- Java 17+
- PostgreSQL 14+
- Maven 3.8+

## Steps to Run

### 1. Clone the repo
```bash
git clone https://github.com/gsvenkatsai/FraudDetection.git
cd FraudDetection
```

### 2. Create the database
```bash
sudo -u postgres psql
```
```sql
CREATE DATABASE frauddetection;
\q
```

### 3. Configure credentials
Edit `src/main/resources/db.properties`:
```properties
db.url=jdbc:postgresql://localhost:5432/frauddetection
db.username=postgres
db.password=yourpassword
```

### 4. Build and run
```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.frauddetection.FraudDetectionApp"
```

### 5. Initialize data
- Click **"Initialize/Reset Database"** button
- Click **"Run Fraud Detection Rules"**
- Go to **Alerts tab** to see results