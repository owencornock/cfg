# Risk Decision Service

A Spring Boot service that accepts client and business application data, calculates a risk score (0-100), and returns a decision: **APPROVE**, **REFER**, or **DECLINE**.

## Tech Stack

- **Java 21** - Latest LTS
- **Spring Boot 3.4.1** - Latest stable release
- **Gradle 8.12** - Build tool (bundled via wrapper)
- **Lombok** - Reduce boilerplate
- **JUnit 5** - Testing framework
- **WireMock 3.10** - External service stubbing for integration tests
- **JaCoCo** - Code coverage reporting

## Project Structure

```
src/
├── main/
│   ├── java/com/example/starter/
│   │   ├── controller/     # REST controllers
│   │   ├── service/        # Business logic (start here!)
│   │   ├── dto/            # Request and response objects
│   │   ├── model/          # Domain models (enums, entities)
│   │   ├── exception/      # Global exception handling
│   │   └── Application.java
│   └── resources/
│       └── application.yml
├── test/                   # Unit tests
│   └── java/
└── integrationTest/        # Integration tests (separate source set)
    ├── java/
    └── resources/
```

## Getting Started

### Prerequisites

- **Java 21 JDK** - Install via one of:
  - [SDKMAN](https://sdkman.io/) (recommended): `sdk install java 21.0.7-amzn`
  - [Adoptium](https://adoptium.net/) (direct download)
  - [Amazon Corretto](https://aws.amazon.com/corretto/)
- That's it - Gradle is bundled via the wrapper, no separate install needed.

### Verify Java

```bash
java -version
# Should show version 21.x.x
```

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

The application starts at http://localhost:8080

### Try It Out

Once the app is running, submit a risk decision request:

```bash
curl -X POST http://localhost:8080/api/v1/risk-decisions \
  -H "Content-Type: application/json" \
  -H "X-Correlation-ID: test-123" \
  -d '{
    "client": { "customerId": "CUST-001", "countryOfApplication": "GB" },
    "business": {
      "legalName": "Acme Trading Ltd",
      "countryOfIncorporation": "UK",
      "dateOfIncorporation": "2015-09-10",
      "annualTurnover": { "amount": 450000, "currency": "GBP" }
    },
    "owners": [{ "fullName": "Jane Doe", "dateOfBirth": "1990-04-15" }],
    "loan": { "requestedAmount": 10000 }
  }'
```

You should get back a `201 Created` response like:

```json
{
  "decisionId": "dec-...",
  "riskDecision": "APPROVE",
  "riskScore": 0,
  "evaluatedFactors": {
    "companyAgePoints": 0,
    "turnoverPoints": 0,
    "ownerAgePoints": 0,
    "loanToTurnoverPoints": 0
  },
  "reasons": [],
  "createdAt": "2026-...",
  "correlationId": "test-123"
}
```

The score is `0` and the decision is `APPROVE` because the scoring logic is stubbed out. **Your task is to implement it.**

Try sending an invalid request to see validation errors:

```bash
curl -X POST http://localhost:8080/api/v1/risk-decisions \
  -H "Content-Type: application/json" \
  -d '{ "client": { "customerId": "" } }'
```

### API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/risk-decisions` | POST | Create a risk decision |
| `/api/v1/health` | GET | Application health check |
| `/actuator/health` | GET | Spring Actuator health |
| `/actuator/info` | GET | Build info |

## Your Task

The service compiles and runs, but the scoring logic returns hardcoded values. Open **`RiskDecisionServiceImpl.java`** and look for the `TODO` comments. Each private method maps to one scoring factor:

| Method | What to implement |
|--------|-------------------|
| `calculateCompanyAgePoints()` | Score based on company age (years since incorporation) |
| `calculateTurnoverPoints()` | Score based on annual turnover amount |
| `calculateOwnerAgePoints()` | Score based on youngest owner's age |
| `calculateLoanToTurnoverPoints()` | Score based on loan-to-turnover ratio |
| `determineDecision()` | Map total score to APPROVE / REFER / DECLINE |
| `buildReasons()` | Add descriptive reasons for the decision |

As you implement each method, add corresponding unit tests in **`RiskDecisionServiceImplTest.java`** (there are TODO comments showing example tests to add).

## Testing

### Unit Tests

```bash
./gradlew test
```

### Integration Tests

```bash
./gradlew integrationTest
```

### All Tests

```bash
./gradlew allTests
```

### Code Coverage Report

After running tests, view the report at:
```
build/reports/jacoco/test/html/index.html
```

## Docker

### Build and Run

```bash
docker build -t risk-decision-service .
docker run -p 8080:8080 risk-decision-service
```

## Build Commands

| Command | Description |
|---------|-------------|
| `./gradlew build` | Full build with tests |
| `./gradlew bootRun` | Run application |
| `./gradlew test` | Run unit tests |
| `./gradlew integrationTest` | Run integration tests |
| `./gradlew allTests` | Run all tests |
| `./gradlew jacocoTestReport` | Generate coverage report |
| `./gradlew clean` | Clean build directory |
| `./gradlew bootJar` | Build executable JAR |

## IDE Setup

### IntelliJ IDEA

1. Import as Gradle project
2. Enable annotation processing for Lombok:
   - Settings → Build → Compiler → Annotation Processors
   - Check "Enable annotation processing"
3. Install Lombok plugin if not already installed

### VS Code

1. Install "Extension Pack for Java"
2. Install "Lombok Annotations Support for VS Code"
3. Reload window after importing project
