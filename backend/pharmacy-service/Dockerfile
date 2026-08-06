# Multi-stage build for Spring Boot service
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY ../pom.xml ../pom.xml
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -f pom.xml

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8085/actuator/health || exit 1
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
