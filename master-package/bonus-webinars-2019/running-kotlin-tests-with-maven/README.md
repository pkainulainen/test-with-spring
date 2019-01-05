# Running Kotlin Tests With Maven

This example demonstrates how we can create a Maven project that can compile and run 
both unit and integration tests which use Kotlin and JUnit 5.

The requirements of our example are:

* All code (application and tests) must use Kotlin.
* Unit and integration tests must have separate source and resource directories.
* It must be possible to run only unit or integration tests.
* It must be possible to run both unit and integration tests.

## Running Unit Tests

We can run our unit tests by using the Maven Surefire plugin. When we want to run
our unit tests, we have to run the following command at command prompt:

    mvn clean test -P dev

## Running Integration Tests

We can run our integration tests by using the Maven Failsafe plugin. When we want to
run our integration tests, we have to run the following command at command prompt:

    mvn clean verify -P integration-test
    
## Running All Tests

When we want to run both unit and integration tests, we have to run the following
command at command prompt:

    mvn clean verify -P all-tests        
