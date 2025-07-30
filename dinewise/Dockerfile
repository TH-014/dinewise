# Use OpenJDK base image hello
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the Maven build files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN chmod +x mvnw
# Download dependencies (layer caching)
RUN ./mvnw dependency:go-offline

# Copy the rest of the source code
COPY . .

RUN chmod +x mvnw

# Package the app
RUN ./mvnw clean package -DskipTests

# Run the jar
CMD ["java", "-jar", "target/dinewise-0.0.1-SNAPSHOT.jar"]
