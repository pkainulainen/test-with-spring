# Writing Integration Tests for Spring Web Applications - REST API

This example demonstrates how you can write unit, integration and end-to-end 
tests for a Spring Boot REST API by using Spock Framework.

**Note:** This example is still work in progress and it doesn't have all tests yet.

## Running the Web Application

You can run the web application by using either Maven or Gradle. After you have 
started the web application, you can open the login page by using the URL:

http://localhost:8080

You can log in by using the following information:

* john.doe@example.com / password - A normal user
* anne.admin@example.com / password - An administrator

### Running the Web Application With Maven

You can run the web application by using the following command:

	mvn clean spring-boot:run -P dev
	
### Running the Web Application With Gradle

You can run the web application by using the following command:

	gradle clean bootRun

## Running Unit Tests

You can run unit tests by using either Maven or Gradle.

### Running Unit Tests With Maven

You can run unit tests by using the following command:

    mvn clean test -P dev

### Running Unit Tests With Gradle

You can run unit tests by using the following command:

	gradle clean test
