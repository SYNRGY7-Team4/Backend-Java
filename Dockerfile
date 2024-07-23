# Stage 1: Build
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn package

# Stage 2: Run
FROM openjdk:17-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/backend-*.jar /app/backend.jar

# Expose the application port
EXPOSE 8080

# List contents of /app for debugging
RUN ls -l /app

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/backend.jar"]
