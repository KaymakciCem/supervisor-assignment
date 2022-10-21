FROM openjdk:17
ADD target/supervisor-assignment-0.0.1-SNAPSHOT.jar supervisor-assignment.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "supervisor-assignment.jar"]
