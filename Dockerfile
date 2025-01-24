# Use the Scala sbt official image
FROM sbtscala/scala-sbt:eclipse-temurin-21.0.5_11_1.10.5_3.5.2

# Set the working directory
WORKDIR /app

# Copy all application files to the container
COPY . ./

# Build the application
RUN sbt stage

# Expose the default Play Framework port
EXPOSE 9000

# Run the application
CMD ["sh", "-c", "./target/universal/stage/bin/prefcards -Dplay.secret=$PLAY_SECRET"]
