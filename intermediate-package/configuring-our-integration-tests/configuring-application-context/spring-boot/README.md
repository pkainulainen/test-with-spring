# Configuring the Application Context of Our Integration Tests

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