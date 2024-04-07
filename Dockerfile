FROM openjdk:17-jdk
LABEL authors="harsh"

COPY target/assignment.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "assignment.jar"]