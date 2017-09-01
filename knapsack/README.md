#

## Run

sh mvnw.sh spring-boot:run

$ curl -v -u user:password -H "Content-Type: application/json" -X POST localhost:8080/api/v1/knapsack/6 -d '[{"weight":5, "price":3}, {"weight":5, "price":4}]'
