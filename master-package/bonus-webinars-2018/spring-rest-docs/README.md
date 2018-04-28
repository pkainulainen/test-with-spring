# Getting Started With Spring REST Docs

This example demonstrates how you can get started with Spring REST Docs.

## Creating the API documentation

You can create API documentation by using one of the following commands:

    mvn clean package -P dev
    mvn clean package -P integration-test
    
Note that the used Maven profile specifies the tests (either unit or integration) which are used to create the snippets which are
included in the created API documentation.    

## Running Tests

### Running Unit Tests

You can run unit tests by using the following command:

    mvn clean test -P dev

### Running Integration  Tests

Even though this example doesn't have meaningful integration tests, you can run integration tests by using the following command:

    mvn clean verify -P integration-test

### Running End-to-End Tests

Even though this example doesn't have meaningful end-to-end tests, you can run end-to-end tests by using the following command:

    mvn clean verify -P end-to-end-test    