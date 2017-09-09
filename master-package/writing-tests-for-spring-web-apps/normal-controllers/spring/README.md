# Writing Tests for Spring Web Applications - Normal Controllers

This example demonstrates how you can write unit, integration, and end-to-end
tests for a normal Spring web application by using Spock Framework.

**NOTE: This is a work in progress and doesn't contain all tests (yet)!**

## Running the Web Application

You can run the web application by using either Maven or Gradle. After you have 
started the web application, you can open the login page by using the URL:

http://localhost:8080

You can log in by using the following information:

* john.doe@example.com / password - A normal user
* anne.admin@example.com / password - An administrator

### Running the Web Application with Maven

You can run the web application by using the following command:

	mvn clean jetty:run -P dev

### Running the Web Application with Gradle

You can run the web application by using the following command:

	gradle clean appRun

## Running Unit Tests

You can run unit tests by using either Maven or Gradle.

### Running Unit Tests With Maven

You can run unit tests by using the following command:

    mvn clean test -P dev

### Running Unit Tests With Gradle

You can run unit tests by using the following command:

	gradle clean test
	
## Running Integration Tests

You can run integration tests by using either Maven or Gradle.

### Running Integration Tests With Maven

You can run integration tests by using the following command:

    mvn clean verify -P integration-test

### Running Integration Tests With Gradle

You can run integration tests by using the following command:

    gradle clean integrationTest    