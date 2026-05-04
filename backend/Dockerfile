# Multi-stage Dockerfile for HR Management System Backend

# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml
COPY pom.xml .

# Copy source code
COPY src src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /app/target/hr-management-app-1.0.0.jar app.jar

# Create logs directory
RUN mkdir -p /app/logs

# Expose port 8080
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application with environment variables
ENTRYPOINT ["java", "-jar", "app.jar"]

