# Online Appointment Scheduling System

A Spring Boot web application for managing tutor appointments with built-in booking protection, real-time metrics, and notification support.

## Features

- **Appointment Management**: Create, view, and cancel appointment slots
- **Double-Booking Prevention**: Atomic database operations ensure slot conflicts are prevented
- **Booking History**: Full audit trail with formatted timestamps
- **Subject Management**: Support for subject-based appointments with dropdown selection
- **Live Metrics**: Track booking success rates and performance metrics
- **Health Checks**: Database and service connectivity monitoring
- **Notification Service**: Mock notification support for booking confirmations

## Requirements

- **Java**: 22.0.1 or higher
- **Build Tool**: Gradle (included via wrapper) or Maven
- **Database**: SQLite (auto-initialized)

## Build and Run

### Using Gradle (recommended - uses wrapper):

```bash
# Build
./gradlew build -x test

# Run
./gradlew bootRun
```

### Using Maven:

```bash
mvn -DskipTests clean package
mvn spring-boot:run
```

## Access the Application

- **Home**: http://localhost:8080/
- **Appointments**: http://localhost:8080/appointments
- **Booking History**: http://localhost:8080/history
- **Add Slots**: http://localhost:8080/slots/new
- **Metrics**: http://localhost:8080/metrics
- **Health**: http://localhost:8080/health

## Project Structure

```
src/
├── main/
│   ├── java/com/appointmentsystem/
│   │   ├── controller/      # REST and view controllers
│   │   ├── service/         # Business logic (BookingService, NotificationService, etc.)
│   │   ├── repository/      # Data access layer
│   │   ├── model/           # Domain entities
│   │   ├── exception/       # Custom exceptions
│   │   └── config/          # Configuration and initialization
│   └── resources/
│       ├── templates/       # Thymeleaf HTML templates
│       ├── schema.sql       # Database schema
│       └── data.sql         # Sample data
└── test/
    └── java/com/appointmentsystem/
        └── [Unit tests]
```

## Database

The application uses SQLite with WAL (Write-Ahead Logging) mode for concurrent access. The database file (`appointments.db`) is automatically created and initialized on first run.


