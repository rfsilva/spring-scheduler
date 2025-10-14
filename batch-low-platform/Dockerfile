FROM maven:3.9.5-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY run.sh /app/run.sh
RUN chmod +x /app/run.sh

# Create a volume for logs and data
VOLUME ["/app/logs", "/app/data"]

# Set entrypoint to the run script
ENTRYPOINT ["/app/run.sh"]

# Default command (can be overridden)
CMD ["--help"]