# Financial Tracker

Demo for Financial Tracker

## Description

This project is a backend application for a financial tracker. It provides APIs for user authentication (registration, login, email confirmation) and managing financial items for users.

## Technologies Used

- Java 21
- Spring Boot 3.5.0
- Spring Security (JWT for authentication)
- Spring Data JPA (with Hibernate)
- PostgreSQL
- Lombok
- Swagger/OpenAPI for API documentation
- Maven

## Prerequisites

- Java JDK 21 or later
- Maven (or use the provided Maven Wrapper)
- PostgreSQL database

## Configuration

1.  **Database**: Configure your PostgreSQL connection details in [`src/main/resources/application.yml`](src/main/resources/application.yml).
    ```yaml
    spring:
      datasource:
        url: jdbc:postgresql://your-db-host:5432/your-db-name
        username: your-db-username
        password: your-db-password
    ```
2.  **Email**: Configure your SMTP server details for email sending (e.g., account activation) in [`src/main/resources/application.yml`](src/main/resources/application.yml).
    ```yaml
    spring:
      mail:
        host: smtp.example.com
        port: 587
        username: your-email@example.com
        password: your-email-password
        properties:
          mail:
            smtp:
              auth: true
              starttls:
                enable: true
    ```

## Build

To build the project, navigate to the project's root directory and run:

```sh
./mvnw clean install
```
(On Windows, use `mvnw.cmd clean install`)

This will compile the code and create a JAR file in the `target/` directory.

## Running the Application

You can run the application using the Spring Boot Maven plugin:

```sh
./mvnw spring-boot:run
```
(On Windows, use `mvnw.cmd spring-boot:run`)

Alternatively, you can run the packaged JAR file:

```sh
java -jar target/financialtracker-0.0.1-SNAPSHOT.jar
```

The application will start, and by default, it will be accessible at `http://localhost:8080`.

## API Documentation

API documentation is available via Swagger UI once the application is running.
You can access it at: `http://localhost:8080/swagger-ui.html`

The OpenAPI specification can be found at:
- `/v3/api-docs` (JSON)
- `/v3/api-docs.yaml` (YAML)

The server URL for the deployed API (as configured in [`OpenApiConfig.java`](src/main/java/com/moneyboss/financialtracker/config/OpenApiConfig.java)) is: `https://moneyboss-1-env-2.eba-xpbh2wsy.eu-north-1.elasticbeanstalk.com`

## API Endpoints

### Authentication ([`AuthenticationController.java`](src/main/java/com/moneyboss/financialtracker/auth/AuthenticationController.java))

-   `POST /register`: Register a new user.
-   `POST /auth`: Authenticate a user and get a JWT token.
-   `POST /activation/resend`: Resend activation email.
-   `POST /activation/confirm`: Confirm email activation with a token.
-   `GET /activation?token={token}`: Activate account via email link.

### Items ([`ItemController.java`](src/main/java/com/moneyboss/financialtracker/item/service/ItemController.java))

-   `POST /items/get-items`: Get items for a user (Requires "user" or "admin" authority).
-   `POST /items/add-user-item`: Add an item for a user (Requires "user" or "admin" authority).
-   `POST /items/add-item`: Add a new item to the system (Requires "admin" authority).

## Project Structure

Key directories and files:

-   `src/main/java`: Main application source code.
    -   `com/moneyboss/financialtracker/auth`: Authentication related classes.
    -   `com/moneyboss/financialtracker/config`: Spring Security, JWT, OpenAPI, and application configurations.
    -   `com/moneyboss/financialtracker/email`: Email sending service.
    -   `com/moneyboss/financialtracker/item`: Item management related classes.
    -   `com/moneyboss/financialtracker/user`: User and Role entities and repositories.
-   `src/main/resources`: Application resources, including `application.yml`.
-   `src/test/java`: Unit and integration tests.
-   `pom.xml`: Maven project configuration.

## How to Contribute

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.