# Use the official OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file and .env file into the container
COPY .env .env
COPY target/WeightliftingApplication-0.0.1-SNAPSHOT.jar app.jar

# Set the command to run your application using the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
