# NTConsult Test
NTConsult Technical Task

## H2
> **_NOTE:_**  http://localhost:8080/h2-console/login.jsp

## Documentation - Swagger
> **_NOTE:_**  http://localhost:8080/swagger-ui/index.html

## Image Docker
> **_NOTE:_**  docker build -t app:1.0 .
 
## Run application docker
> **_NOTE:_**  docker run -p 8080:8080 app:1.0

## curls test
> **_NOTE:_**  curl --location 'http://localhost:8080/api/votes/topics' \
--header 'Content-Type: application/json' \
--data '{
"name": "Pauta sobre sustentabilidade"
}'

> **_NOTE:_**  curl --location --request POST 'http://localhost:8080/api/votes/topics/1/open-session' \
--header 'durationInSeconds: 500' \
--data ''


> **_NOTE:_**  curl --location 'http://localhost:8080/api/votes/topics/1/votes' \
--header 'Content-Type: application/json' \
--data '{
"associateId": "01234567890",
"vote": "Sim"
}'

## Application performance
> **_NOTE:_**  The application performance was tested using JMeter, in the file "Argyle Fibonacci Test.jmx" you will find the test plan.

## Tests with the image
> **_NOTE:_**  With this README, you have clear instructions for running the application, exploring the API documentation, and conducting performance testing. Additionally, the Docker image luisbsb/argyle:1.0 is available on Docker Hub for testing purposes.
