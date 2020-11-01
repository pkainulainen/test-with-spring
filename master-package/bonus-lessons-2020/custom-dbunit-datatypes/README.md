# Writing Custom DbUnit Data Types

This example application demonstrates how you can write custom
DbUnit data types and use them when you write integration tests 
for your Spring and Spring Boot applications.

This example implements two custom DbUnit data types:

* The `JsonDataType` provides support for the PostgreSQL's `json` data type.
* The `StringArrayDataType` provides for the PostgreSQL's `array` data type when
  the array is question is a `text` array. 

**Note: This example application assumes that you are using Java 15!**
  
## Running the tests

You can run integration tests with Maven by running the command:

    mvn clean verify -P integration-test
    
## Running the application

Even though it's possible to try to run this application, you shouldn't do this because it won't start.