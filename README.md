# HR Management Web Application

A production-ready HR Management system with a focus on Employee Timesheets, Payroll, and Compensation Rate Calculator (CRC).

## Features

- **Employee Management**: CRUD operations, role and department management
- **Timesheet Management**: Daily/weekly time entry with manager approval workflow
- **Compensation Rate Calculator (CRC)**: Interactive pay calculation with real-time preview
- **Payslip Generation**: PDF generation with detailed breakdown
- **Dashboard**: Analytics with employee count, payroll, pending approvals, expense graphs
- **Security**: JWT authentication with role-based access control
- **Modern UI**: React + TypeScript with SaaS-like design

## Tech Stack

### Backend
- Java 17
- Spring Boot 3.x
- Spring Data JPA / Hibernate
- Spring Security + JWT
- PostgreSQL
- Swagger/OpenAPI
- iText (PDF generation)

### Frontend
- React 18
- TypeScript
- Tailwind CSS
- Recharts (for graphs)
- Axios (HTTP client)
- React Router

### DevOps
- Docker (multi-stage builds)
- Docker Compose
- Environment-based configs

## Project Structure

```
hr-management-app/
├── backend/               # Spring Boot REST APIs
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/hrms/
│   │   │   │   ├── config/          # Security, JPA, Swagger configs
│   │   │   │   ├── controller/      # REST endpoints
│   │   │   │   ├── service/         # Business logic
│   │   │   │   ├── repository/      # JPA repositories
│   │   │   │   ├── entity/          # JPA entities
│   │   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── exception/       # Custom exceptions
│   │   │   │   ├── util/            # Utilities
│   │   │   │   └── HrmsApplication.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       ├── application-prod.yml
│   │   │       └── db/
│   │   │           └── migration/   # Flyway SQL migrations
│   │   └── test/
│   ├── pom.xml
│   ├── Dockerfile
│   └── docker-compose.yml
│
├── frontend/              # React + TypeScript
│   ├── src/
│   │   ├── components/    # Reusable components
│   │   ├── pages/         # Page components
│   │   ├── services/      # API services
│   │   ├── hooks/         # Custom hooks
│   │   ├── types/         # TypeScript types
│   │   ├── context/       # React context (auth, etc.)
│   │   ├── utils/         # Utilities
│   │   ├── App.tsx
│   │   └── index.tsx
│   ├── package.json
│   ├── tsconfig.json
│   ├── Dockerfile
│   └── .env.example
│
└── docs/
    ├── API.md             # API endpoints documentation
    ├── DATABASE.md        # Schema documentation
    └── SETUP.md           # Installation guide
```

## Quick Start

### Prerequisites
- Java 17+
- Node.js 18+
- PostgreSQL 14+
- Docker & Docker Compose

### Setup

1. **Backend**
   ```bash
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```

2. **Frontend**
   ```bash
   cd frontend
   npm install
   npm start
   ```

3. **Database**
   ```bash
   docker-compose up -d db
   ```

## API Endpoints

See [API.md](./docs/API.md) for complete endpoint documentation.

## Database Schema

See [DATABASE.md](./docs/DATABASE.md) for schema details.

## Security

- JWT-based authentication
- Role-based access control (ADMIN, HR_MANAGER, EMPLOYEE)
- HTTPS in production
- Secure password hashing (BCrypt)

## Testing

```bash
# Backend
mvn test

# Frontend
npm test
```

## Deployment

```bash
docker-compose up -d
```

## License

MIT

