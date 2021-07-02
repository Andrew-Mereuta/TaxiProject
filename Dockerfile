#FROM openjdk:11
#ADD target/taxi-project.jar taxi-project.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "taxi-project.jar"]
FROM openjdk:11
# Copy jar file
COPY target/*.jar  /opt/docker-spring-boot.jar
ADD wrapper.sh wrapper.sh
#RUN apk add --no-cache bash
RUN bash -c 'chmod +x /wrapper.sh'
ENTRYPOINT ["/bin/bash", "/wrapper.sh"]