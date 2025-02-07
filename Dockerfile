# Use Zulu OpenJDK 17 as the base image
FROM azul/zulu-openjdk:17-latest

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file built by GitHub Actions into the Docker image
COPY build/libs/todo-*-SNAPSHOT.jar /app/todo.jar

# Expose the port that the application will run on
EXPOSE 8080

# Run the main jar file (avoiding the plain jar)
CMD ["sh", "-c", "java -jar /app/todo.jar"]
