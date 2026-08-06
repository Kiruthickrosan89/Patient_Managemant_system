# Multi-stage build for Spring Boot service
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy parent POM first (for dependency caching)
COPY ../pom.xml ../pom.xml

# Copy this service
COPY pom.xml .
COPY src ./src

# Build with Maven (skip tests for faster builds)
RUN mvn clean package -DskipTests -f pom.xml

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8761/actuator/health || exit 1

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
