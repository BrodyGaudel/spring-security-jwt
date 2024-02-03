Demonstration of a user management and authentication application using Java 17, Spring Boot, Spring Security, JSON Web Token (JWT), and MySQL:

```markdown
# User Management and JWT Authentication Application

This demonstration showcases a Java application using Spring Boot and Spring Security to create a user management system with authentication based on JSON Web Token (JWT). User data is stored in a MySQL database.

## Prerequisites

Make sure the following are installed on your machine before running the application:

- Java 17
- Maven
- MySQL

## Database Configuration

1. Create a MySQL database with a name of your choice (e.g., `user_management_db`).
2. Update the database information in the `src/main/resources/application.properties` file.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/users/*
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## JWT Configuration

JWT parameters can be configured in the `src/main/resources/application.properties` file. You can adjust the token validity duration and the secret key used for signing and verifying tokens.

```properties
jwt.secret=YourSecretKey
jwt.expirationMs=3600000
```

## Compilation and Execution

1. Clone this repository to your machine.
2. Navigate to the project directory.
3. Run the Maven command to compile and launch the application.

```bash
mvn spring-boot:run
```

The application will be accessible at http://localhost:8080.

## Usage Examples

You can use tools like [Postman](https://www.postman.com/) to test various functionalities of the application. Make sure to follow the authentication flow to obtain the JWT token before accessing protected resources.

## Contributors

- Brody Gaudel MOUNANGA BOUKA

Feel free to contribute by opening issues, proposing pull requests, or adding new features.
