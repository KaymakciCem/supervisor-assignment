# supervisor-assignment

___
### Spring Boot Application

---
This project helps us to create the hierarchy between employees.

#### Requirements
- The API exposes two endpoints.
- first one generates the hierarchy between the employees and sends it back to the user as a json response
- Second one helps us to retrieve the two supervisors for the given employee


request for creating the hierarchy based on a given input
```curl
curl --location --request POST 'http://localhost:8087/hierarchy' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=032479975135B169B4E12357800F46BE' \
--data-raw '{
    "Pete": "Nick",
    "Nick": "Sophie",
    "Barbara": "Nick",
    "Sophie": "Jonas"
}'
```

retrieve the two supervisors of the given employee
```curl
curl --location --request GET 'http://localhost:8087/hierarchy/supervisors/Barbara' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--header 'Cookie: JSESSIONID=032479975135B169B4E12357800F46BE'
```

### Tech Stack

---
- Java 17
- Spring Boot
- Spring Data JPA
- Restful API
- H2 In memory database (for development usage)
- Mysql Db
- Docker
- JUnit 5

### Prerequisites

---
- Maven

### Run & Build

first go to the terminal and open up the project directory. "~/supervisor-assignment/"

### Run the tests

mvn test

### Run project

mvn spring-boot:run

### Docker

to create the docker container execute the following commands on project directory terminal
```
mvn clean package docker:build (building the project)
docker build . -t supervisor-assignment (creates image)
docker-compose up -d (running the project)
```

to stop the project
```
docker-compose down
```