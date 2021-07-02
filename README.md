# Taxi Project
This is very simplified taxi project. You can add new clients, drivers. After that clients can order a drive. 
When you register a driver you have to add the model of his car. Clients can see all their orders. Drivers can
also see history of their orders. Both clients and drivers can change their name and password. Drivers can also
change their car, if they bought new one. In general the application is secured with JWT(https://jwt.io/), besides 
I decided to divide access to particular endpoints so that clients cannot change drivers car (or anything else),
and drivers cannot change clients' names. But, there is one more role and this is Admin. Admin have full access
to all resources and can perform any action.

Below you can see the er diagram of the project.  

## ER Diagram of my project

![ER-Taxi-Project.jpg](ER-Taxi-Project.jpg)

## Technologies I used

Spring Boot, Spring data JPA, Spring Security, MySQL, JSON Web Token, Maven, Jenkins, Docker

## How to start the program locally

Prerequisites:

* Maven should be installed (check it with command: mvn -v)
* MySQL should be installed 
* Jenkins should be installed and configured
* Docker should be installed (if you use Windows OS you will have to install WSL)
* Postman, Newman nad Jmeter should be installed and configured (this is for testing)

When all requirements are met, you can clone the repo:

```shell
git clone https://github.com/Andrew-Mereuta/TaxiProject.git
```

The repository is cloned, now use command prompt to change directory to the one in which you cloned 
the project. Run there commands:

```shell
mvn clean install

mvn spring-boot:run
```

The application is now running, you use postman and try to access the endpoints. For example:
localhost:8080/test (Use get request)


## How to run with docker

Prerequisites: 
- Make sure you have docker installed on your machine (to check it, enter command prompt and type: docker -v)

Execute the following commands:

```shell
docker pull andrewmereuta/taxi-project

docker-compose --file docker-compose.yml up --detach
```






## Contributors
| 📸 | Name | Email |
|---|---|---|
| <img src="https://cdn.discordapp.com/attachments/812702649295437824/826515958239330344/1617127142670.jpg" width="100" height="154" /> | Andrei Mereuta | mereuta.andrew@gmail.com |
