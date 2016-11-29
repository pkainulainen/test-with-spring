# Configuring Spring MVC Test

## Running the Web Application

You can run the web application by using Maven or Gradle.

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
	
## Running Integration Tests

You can run integration tests by using either Maven or Gradle.

### Running Integration Tests With Maven

    mvn clean verify -P integration-test