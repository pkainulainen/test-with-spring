# Writing Integration Tests With JUnit 5 and TestContainers

This example demonstrates how you can write integration tests for a Spring Boot web application 
with JUnit 5 and TestContainers. To be more specific, you will learn to write
integration tests which use a PostgreSQL database that is started and stopped by TestContainers.

## Running Unit Tests

Even though this example doesn't have meaningful unit tests, you can run unit tests by using the following command:

    mvn clean test -P dev

## Running Integration  Tests

Even though this example doesn't have meaningful integration tests, You can run integration tests by using the following command:

    mvn clean verify -P integration-test