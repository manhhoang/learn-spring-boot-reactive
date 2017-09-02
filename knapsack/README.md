#

## Setup
    1. JAVA_HOME
    2. Download Maven: http://apache.xl-mirror.nl/maven/maven-3/3.5.0/binaries/apache-maven-3.5.0-bin.zip  
    3. Unzip apache-maven-3.5.0-bin.zip
    3. M2_HOME: set M2_HOME=E:\Working\Tools\apache-maven-3.5.0

## Run

$ mvn clean install

sh mvnw.sh spring-boot:run

$ curl -v -u user:password -H "Content-Type: application/json" -X POST localhost:8080/api/v1/knapsack/6 -d '[{"weight":5, "price":3}, {"weight":5, "price":4}]'

http://localhost:8080
