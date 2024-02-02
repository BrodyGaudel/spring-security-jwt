# Definition of the base image
FROM openjdk:17-ea-10-jdk-alpine

# Definition of the maintainer and author of the image
LABEL authors="Brody Gaudel MOUNANGA BOUKA"
LABEL maintainer="Brody Gaudel https://github.com/BrodyGaudel"

# Setting environment variables
ENV MYSQL_USER=root
ENV MYSQL_PWD=brody2250
ENV MYSQL_HOST=localhost
ENV MYSQL_PORT=3306
ENV MYSQL_DATABASE=security_db
ENV ALLOWED_ORIGINS=http://localhost:4200,http://localhost:3000

# Copying your jar application into the container
COPY target/security-service-0.0.1.jar security-service.jar

# Command to launch the application when running the container
ENTRYPOINT ["java","-jar","security-service.jar"]
