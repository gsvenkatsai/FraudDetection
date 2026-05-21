# Build stage
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM jetty:9.4-jre17
COPY --from=build /app/target/fraud-detection-system-1.0-SNAPSHOT.war /var/lib/jetty/webapps/ROOT.war
EXPOSE 8080
