# Task REST Client

This project demonstrates how we can implement a data provider service that
fulfills the contract that is implemented with Spring Cloud Contract and JUnit 5.

You can run the tests by running the following command at command prompt:

    mvn clean test

You can run the application by running the following command at command prompt:

    mvn clean spring-boot:run
    
After you have started the application, you can test our REST API manually 
by sending a `GET` request to the url: _http://localhost:9000/api/task/{id}_. 
Note that you have replace the `{id}` placeholder with the id of the requested
task (use either 1 or 2).
        