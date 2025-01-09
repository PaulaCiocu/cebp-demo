# Build stage
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY . ./
RUN ./mvnw clean package

# Runtime stage
FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y xdg-utils
WORKDIR /app
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8000
CMD ["java", "-jar", "app.jar"]
