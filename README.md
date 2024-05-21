# hexagonal-spring-boot
Hexagonal Architecture on Java Spring Boot for managing an e-commerce, and React with Tailwind on the Frontend.

![wallpaper.jpg](wallpaper.jpg)

## What the demos!

- [User Management Demo](user%20management.mov)
- [Product Management Demo](product%20management.mov)
- [Time Management Demo](time%20management.mov)
- [Email Management Demo](message%20management.mov)
- [Meeting Management Demo](meeting%20management.mov)
- [Sign Up Demo](sign%20up.mov)

## Introduction
 
* Read the paper [here](./Paper.pdf)

![Paper.png](./Paper.png)

This application relies on Java Spring Boot Hexagonal Architecture:

- Domain Models: Defines the core domain models used throughout the application [here](./app/src/main/java/com/martincastroalvarez/hex/hex/domain/models).
- Domain Out Ports: Includes repository interfaces which define the expected behaviors of data access mechanisms [here](./app/src/main/java/com/martincastroalvarez/hex/hex/domain/ports/out).
- Domain Exceptions: Custom exceptions that handle specific domain errors [here](./app/src/main/java/com/martincastroalvarez/hex/hex/domain/exceptions).
- Domain Service Interfaces: Interfaces for the domain services that encapsulate business logic [here](./app/src/main/java/com/martincastroalvarez/hex/hex/domain/services).
- Unit Tests: Contains unit tests that verify the behavior of the application components [here](./app/src/test/java/com/martincastroalvarez/hex/hex).
- Configuration: Application configurations and properties [here](./app/src/main/java/com/martincastroalvarez/hex/hex/config).
- Web API Adapters (Controllers): Adapters that expose the application functionality over HTTP as web APIs [here](./src/main/java/com/martincastroalvarez/hex/hex/adapters/web).
- Database Adapters (JPA Repositories): Database adapters that implement the repository interfaces with JPA [here](./app/src/main/java/com/martincastroalvarez/hex/hex/adapters/db).
- Adapter Model Entities: Entity models used by JPA to map to the database tables [here](./app/src/main/java/com/martincastroalvarez/hex/hex/adapters/entities).
- Mappers for Model to Entity Mapping and Vice Versa: Utility classes to convert between domain models and JPA entities [here](./app/src/main/java/com/martincastroalvarez/hex/hex/adapters/mappers).
- Models Application (Service Implementations): Implementation of the domain services, where the business logic is actually carried out, [here](./app/src/main/java/com/martincastroalvarez/hex/hex/application).
- Web application: The React application is implemented [here](./web).

## Documentation

### Installation

* Install Java

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
java -version
```

* Install Maven

```bash
brew install maven
mvn -v
```

* Install the frontend dependencies

```bash
cd web/
pnpm i
```

* Install Docker Compose

```bash
sudo curl -L "https://github.com/docker/compose/releases/download/v2.17.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
sudo systemctl enable docker
sudo systemctl start docker
```

### Development

* Run the application

```bash
cd app/
mvn spring-boot:run
```

* Access the application at [http://localhost:8080/](http://localhost:8080/)

* Test the application:

```bash
cd app/
mvn test
```
```bash
[...]
[INFO] Tests run: X, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 6.056 s -- in com.martincastroalvarez.hex.hex.RunCommandTests
[INFO] Running com.martincastroalvarez.hex.hex.adapters.web.ProductControllerTest
[INFO] Tests run: X, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.200 s -- in com.martincastroalvarez.hex.hex.adapters.web.ProductControllerTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: X, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  8.120 s
[INFO] Finished at: 2024-05-06T13:39:45-04:00
[INFO] ------------------------------------------------------------------------
```

* Start the React app

```bash
cd web/
REACT_APP_API_URL=http://localhost:8080 npm start
```

* Try logging in using the API.

```bash
RESPONSE=$(curl -s -X POST "http://localhost:8080/auth/login" -H "Content-Type: application/json" -d '{"email": "user@test.com", "password": "test"}')
TOKEN=$(echo $RESPONSE | jq -r '.Token')
echo $RESPONSE | jq
```
```bash
{
  "Token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQHRlc3QuY29tIn0.vgHfw5gAI9avcrqH_Qt_yrMAroaD_ey0IxouNCkX8DpvBgW2_a_gyFFZ38tqlzchkcjGvoE5uuXc-KhXNgww3Q"
}
```

* Try listing existing products.

```bash
curl -s -X GET -H "Authorization: Bearer $TOKEN" "http://localhost:8080/products?page=0&size=3&sort=id&asc=true" | jq
```
```bash
[
  {
    "id": 19,
    "name": "Product 1",
    "isActive": true,
    "price": 10,
  },
  {
    "id": 20,
    "name": "Product 2",
    "isActive": true,
    "price": 50,
  },
  {
    "id": 21,
    "name": "Product 3",
    "isActive": true,
    "price": 50,
  }
]
```

### Deployment

* Build the Java Spring Boot application

```bash
cd app/
mvn clean package
```

* Build the React application

```bash
cd web/ 
REACT_APP_API_URL=http://54.189.152.54:8080 npm run build
```

* Upload the React application to AWS S3.

```
cd web/build/
s3.post s3://com.martincastroalvarez.aec/
```

* Deploy the changes to the EC2 instance.

```bash
ssh -i "hex.pem" ec2-user@ec2-54-189-152-54.us-west-2.compute.amazonaws.com
```

* Start the application using Docker Compose

```bash
docker-compose up --build -d
```

* Visit the login page

![login.png](login.png)

* Visit the signup page

![signup.png](signup.png)

* Visit the users management page

![users.png](users.png)

* Visit the homepage 

![homepage.png](homepage.png)

* Visit the schedule management page

![schedule.png](schedule.png)

* Visit the meeting management page

![meeting.png](meeting.png)

* Visit the product management page

![product.png](product.png)
