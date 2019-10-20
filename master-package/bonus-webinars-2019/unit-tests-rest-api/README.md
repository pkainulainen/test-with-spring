# Writing Unit Tests for a Spring MVC REST API

This example demonstrates how we can write unit tests for a Spring
MVC REST API when we are using Kotlin and JUnit 5.

**This example isn't finished yet. It's missing a lot of tests and 
the system under test needs a few improvements as well**.

## Running Unit Tests

We can run our unit tests by using Maven or Gradle. 

When we want to run our unit tests with Maven, we have to run the following 
command at command prompt:

    mvn clean test -P dev

When we want to run our unit tests with Gradle, we have to run the following 
command at command prompt:

    gradle clean test

## Running Integration Tests

We can run our integration tests by Maven or Gradle. 

When we want to run our integration tests with Maven, we have to run the following 
command at command prompt:

    mvn clean verify -P integration-test

When we want to run our integration tests with Gradle, we have to run the following
command at command prompt:

    gradle clean integrationTest
    
## Running All Tests

When we want to run both unit and integration tests with Maven, we have to run the 
following command at command prompt:

    mvn clean verify -P all-tests      
    
When we want to run both unit and integration tests with Gradle, we have to run the 
following command at command prompt:

    gradle clean test IntegrationTest       

or we can use the command:

    gradle clean build
      
