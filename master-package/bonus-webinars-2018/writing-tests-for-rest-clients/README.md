# Writing Tests for REST Clients With JUnit 5

## Running Unit Tests

You can run unit tests by using the following command:

    mvn clean test -P dev

## Running Integration  Tests

Even though this example doesn't have meaningful integration tests, you can run integration tests by using the following command:

    mvn clean verify -P integration-test

## Running End-to-End Tests

Even though this example doesn't have meaningful end-to-end tests, you can run end-to-end tests by using the following command:

    mvn clean verify -P end-to-end-test    