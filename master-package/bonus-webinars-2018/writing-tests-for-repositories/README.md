# Writing Integration Tests for Spring Powered Repositories With JUnit 5

## Running Unit Tests

Even though this example doesn't have meaningful unit tests, you can run unit tests by using the following command:

    mvn clean test -P dev

## Running Integration  Tests

Even though this example doesn't have meaningful integration tests, You can run integration tests by using the following command:

    mvn clean verify -P integration-test

## Running End-to-End Tests

You can run end-to-end tests by using the following command:

    mvn clean verify -P end-to-end-test    