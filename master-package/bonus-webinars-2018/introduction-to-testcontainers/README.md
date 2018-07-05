# Introduction to TestContainers

This example demonstrates how you can start Docker containers before your tests 
are run and destroy the started containers after your tests have been run. 

Before you can run the tests provided by this example, you have to 
[install the prequisites of the TestContainers library](https://www.testcontainers.org/usage.html#prerequisites).

Also, if you are using macOS or Windows, you might want to take a look at the 
[Compatibility section of the TestContainers User's Manual](https://www.testcontainers.org/compatibility.html).

## Running Tests

You can run tests by using the following command:

    mvn clean test
