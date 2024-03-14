# Webapp


This Spring Boot application serves as an Web Application , providing functionality for users to add, modify user details. It supports the submission of their details. The system also features user authentication, implemented through basic HTTP authentication using username and password, ensuring secure and controlled access. 

Additionally, it is integrated with a CI, with the CI process including checks to ensure that integration tests are successfully executed, guaranteeing robustness and reliability of the deployments. 


## CI/CD Pipeline 1

### Continuous Integration (CI)

 CI pipeline is designed to ensure the code quality and functionality of the application:

- **Pull Request Checks**: Before merging any pull request, our CI system performs the following checks:
  - Code Quality: It checks for integration tests to ensure that new code changes don't break existing functionality.
  

## Technologies Used

- **Spring Boot**: Rapid application development.
- **Hibernate (ORM)**: Simplify database operations.
- **BCrypt**: Secure password hashing.
- **GitHub Actions**: Automated CI pipeline.

## API Endpoints

### Health Check

- `/healthz`: Performs a health check, verifying the connection to the database.

### Assignment Endpoints

- `GET /v1/user/self`: Retrieve the user details by using their username and password.
- `PUT /v1/user/self`: Update the user details by giving in the user credentials.
- `POST /v1/user`: Create a new user with the following validations:
  - User name must be a non-empty string and should be in the format of email.
  - should have first name and last name.
  - password field should not be null.
  - first name and last name should not be null.

## Prerequisites

Before you begin, ensure that you have the following prerequisites installed on your system:

1. **Java Development Kit (JDK)**:
   - Install Java JDK version 17 on your machine. You can download it from [Oracle](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html).

2. **MySQL Database**:
   - Install MySQL on your local machine. You can download it from [MySQL Downloads](https://dev.mysql.com/downloads/installer/).

3. **Spring Boot Project Setup**:
   - Set up a Spring Boot project either using Spring Initializer or manually. You can follow the Spring Boot documentation for project setup.

## Installation

Follow these steps to build and run your Spring Boot application:

1. **Clone the Application Source Code**:
   - Clone the source code for your Spring Boot application from your repository.

2. **Configure MySQL Connection**:
   - Open the `application.properties` file in your project and configure the MySQL database connection properties. Update the following properties with your local MySQL settings:
     - `spring.datasource.url`
     - `spring.datasource.username`
     - `spring.datasource.password`

3. **Build the Application**:
   - Open a terminal/command prompt and navigate to your project's root directory.

   - Build the application using Maven with the following command:
     ```shell
     mvn install -DskipTests
     ```
     This command will compile your code, run tests, and package your application into a JAR.

4. **Run the Application**:
   - Once the build is successful, you can run the JAR file using the following command:
     ```shell
     java -jar target/your-application-name.jar
     ```
     Replace `your-application-name` with the actual name of your JAR file.

5. **Access the Application**:
   - Your Spring Boot application should now be running. You can access it by opening a web browser and navigating to the application URL (e.g., `http://localhost:8080`).
