# Lombok


## Setup MacOS
    1. set JAVA_HOME
    2. Download Maven: http://apache.xl-mirror.nl/maven/maven-3/3.5.0/binaries/apache-maven-3.5.0-bin.zip  
    3. Unzip apache-maven-3.5.0-bin.zip
    3. set M2_HOME=E:\Working\Tools\apache-maven-3.5.0


## Run application on MacOS

$ mvn clean install

$ sh mvnw.sh spring-boot:run

$ curl -v -u user:password -H "Content-Type: application/json" -X POST localhost:8080/api/v1/knapsack/5 -d '[{"weight":4, "price":6}, {"weight":2, "price":4}]'

$ curl -v -u user:password -H "Content-Type: application/json" -X POST localhost:8080/api/v1/knapsack/6 -d '[{"weight":5, "price":3}, {"weight":4, "price":6}, {"weight":2, "price":4}]'


## UI
http://localhost:8080/login


## Swagger UI
http://localhost:8080/swagger-ui.html

http://localhost:8080/v2/api-docs


## Run Docker
$ docker pull manhhoang/learn-spring-boot-reactive

$ docker run -d -p 8080:8080 manhhoang/learn-spring-boot-reactive

$ docker ps -a
$ docker stop [container]
$ docker start [container]


## Technologies

    1. Java 8
    2. Spring Boot
    3. Spring Data Rest
    4. Spring JPA
    5. Spring Security
    6. H2 Database
    7. ReactJS 
    8. Maven