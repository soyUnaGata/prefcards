# Use a lightweight JDK base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy your app's code
COPY . .

# Install sbt
RUN curl -L -o sbt.deb https://github.com/sbt/sbt/releases/download/v1.8.3/sbt-1.8.3.deb && \
    dpkg -i sbt.deb && \
    sbt compile

# Expose the app's port
EXPOSE 9000

# Start the app
CMD ["sbt", "run", "-Dhttp.port=$PORT"]
