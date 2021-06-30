FROM openjdk:11
ADD target/taxi-project.jar taxi-project.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "taxi-project.jar"]