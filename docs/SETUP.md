# Setup & Installation Guide

## Prerequisites

- **Java**: 17 or higher
- **Node.js**: 18 or higher
- **Maven**: 3.9 or higher
- **PostgreSQL**: 14 or higher
- **Docker**: (Optional, for containerized deployment)
- **Git**: For version control

## Installation & Quick Start

### Option 1: Docker Compose (Recommended)

This is the easiest way to get everything running.

```bash
# Navigate to the project root
cd hr-management-app

# Start all services (PostgreSQL, Backend, Frontend)
docker-compose up -d

# Services will be available at:
# - Frontend: http://localhost:3000
# - Backend API: http://localhost:8080/api/v1
# - API Docs (Swagger): http://localhost:8080/swagger-ui.html
# - Database: localhost:5432
```

**Login Credentials:**
- Admin: `admin` / `admin123`
- HR Manager: `hr` / `hr123`
- Employee: `emp` / `emp123`

### Option 2: Manual Setup

#### 1. Database Setup

```bash
# Create PostgreSQL database
createdb hrms_db

# Create database user
psql -U postgres -d hrms_db

# In PostgreSQL shell:
# CREATE USER hrms_user WITH PASSWORD 'hrms_password';
# ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO hrms_user;
# ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO hrms_user;
```

#### 2. Backend Setup

```bash
# Navigate to backend directory
cd backend

# Build the application
mvn clean install

# Run the application
mvn spring-boot:run

# Or package and run:
mvn clean package
java -jar target/hr-management-app-1.0.0.jar
```

The backend will be available at `http://localhost:8080/api/v1`

API Documentation (Swagger) at `http://localhost:8080/swagger-ui.html`

#### 3. Frontend Setup

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Create .env file from example
cp .env.example .env

# Update .env if needed (default should work for local development)
# VITE_API_URL=http://localhost:8080/api/v1

# Start development server
npm run dev

# Frontend will be available at http://localhost:3000
```

## Environment Configuration

### Backend (application.yml)

Key environment variables:
```yaml
SPRING_DATASOURCE_URL: jdbc:postgresql://localhost:5432/hrms_db
SPRING_DATASOURCE_USERNAME: hrms_user
SPRING_DATASOURCE_PASSWORD: hrms_password
JWT_SECRET: your-secret-key-change-in-production
SPRING_PROFILES_ACTIVE: dev
```

### Frontend (.env)

```
VITE_API_URL=http://localhost:8080/api/v1
```

## Database Migrations

Migrations are handled automatically by Flyway. When the application starts, it will:
1. Check for pending migrations in `src/main/resources/db/migration/`
2. Apply them in order

To manually run migrations:
```bash
mvn flyway:migrate
```

## Building for Production

### Backend

```bash
# Build JAR
mvn clean package -DskipTests

# JAR will be at: target/hr-management-app-1.0.0.jar
```

### Frontend

```bash
# Build optimized bundle
npm run build

# Output will be in: dist/
# Deploy to web server or CDN
```

### Docker Build

```bash
# Build Docker image
docker build -t hrms-backend:latest ./backend

# Or use Docker Compose
docker-compose build

# Run container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/hrms_db \
  -e SPRING_DATASOURCE_USERNAME=hrms_user \
  -e SPRING_DATASOURCE_PASSWORD=hrms_password \
  hrms-backend:latest
```

## Troubleshooting

### Port Already in Use

If port 8080 or 3000 is already in use:

**Backend (Port 8080):**
```bash
# Linux/Mac
lsof -i :8080
kill -9 <PID>

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

**Frontend (Port 3000):**
```bash
# Linux/Mac
lsof -i :3000
kill -9 <PID>

# Windows
netstat -ano | findstr :3000
taskkill /PID <PID> /F
```

### Database Connection Issues

Verify connection settings:
```bash
# Test PostgreSQL connection
psql -h localhost -U hrms_user -d hrms_db

# Or use pgAdmin GUI
```

### Dependencies Not Downloaded

Clear Maven cache and rebuild:
```bash
mvn clean install -U
```

### Frontend Build Issues

Clear node_modules and reinstall:
```bash
rm -rf node_modules package-lock.json
npm install
```

## Testing

### Backend Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=CompensationCalculatorServiceTest

# Run with coverage
mvn test jacoco:report
```

### Frontend Tests

```bash
# Run tests (if configured)
npm test

# Run with coverage
npm test -- --coverage
```

## Development Tips

### Frontend Development

- Use `npm run dev` for hot reload development
- Check browser console (F12) for errors
- Use React Dev Tools browser extension for debugging
- All API requests are logged in network tab

### Backend Development

- Use IDE debugger (IntelliJ, VS Code) to set breakpoints
- Check application logs in console for errors
- Use Swagger UI to test APIs at http://localhost:8080/swagger-ui.html
- Database logs can be enabled in application.yml

## Additional Resources

- [API Documentation](./API.md) - Complete list of endpoints
- [Database Schema](./DATABASE.md) - Entity relationships
- [Architecture Guide](./ARCHITECTURE.md) - Code structure and design patterns

## Support

For issues or questions:
1. Check the logs first
2. Review API documentation
3. Verify database connection
4. Check that ports are available
5. Ensure all prerequisites are installed

## Production Deployment

For production deployment:
1. Use strong JWT secret
2. Enable HTTPS
3. Use environment-based configuration
4. Set up SSL certificates
5. Configure firewall rules
6. Use managed database service
7. Implement rate limiting
8. Set up monitoring and logging
9. Use CI/CD pipeline
10. Regular backups

See docker-compose.yml for production-grade example.

