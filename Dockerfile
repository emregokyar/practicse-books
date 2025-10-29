# Building the app
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copying maven files
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Downloading maven dependincies
RUN ./mvnw dependency:go-offline -B
COPY src ./src

# Build the application using Maven
RUN ./mvnw clean package -DskipTests

# Running the app
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copying the jar file from build, which is located in target
# Copy the built JAR file from the previous stage to the container
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]