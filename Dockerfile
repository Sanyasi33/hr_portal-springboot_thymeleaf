## Use a base image with Java 17 (or your required version)
#FROM openjdk:17-jdk-alpine
#
## Set the working directory inside the container
#WORKDIR /app
#
## Copy the JAR file to the container
#COPY ./target/HrPortalThymeleaf.jar app.jar
#
## Expose the port your application runs on (usually 8080)
#EXPOSE 8080
#
## Run the JAR file
#ENTRYPOINT ["java", "-jar", "app.jar"]



# Use the official Maven image to build the application
FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

# Copy the pom.xml file first
COPY pom.xml .

# Copy the source code
COPY src ./src

# Package the application (skip tests if you want faster builds)
RUN mvn clean package -DskipTests

# Use the official OpenJDK image to run the application
FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/HrPortalThymeleaf.jar HrPortalThymeleaf.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "HrPortalThymeleaf.jar"]

