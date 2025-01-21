# Use a lightweight JDK base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the application code to the container
COPY . .

# Build the app
RUN sbt stage

# Expose the app's port
EXPOSE 9000

# Command to run the app
CMD ["target/universal/stage/bin/<your-app-name>", "-Dhttp.port=$PORT"]