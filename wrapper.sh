#!/bin/bash
while ! exec 6<>/dev/tcp/${DATABASE_HOST}/${DATABASE_PORT}; do
    echo "Trying to connect to MySQL at ${DATABASE_HOST}:${DATABASE_PORT}..."
    sleep 10

done
echo ">> connected to MySQL database! <<"
java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=container -jar /opt/docker-spring-boot.jar