# Use a base image with OpenJDK installed
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/players-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that the application will run on
EXPOSE 8080

# Set the command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]