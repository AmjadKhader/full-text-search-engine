# Use a base image with JDK
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file to the Docker image
COPY target/engine-1.0.0.jar text-search-engine.jar

# Expose the port the application runs on
EXPOSE 8092

# Set the entry point for the Docker container
ENTRYPOINT ["java", "-jar", "text-search-engine.jar"]