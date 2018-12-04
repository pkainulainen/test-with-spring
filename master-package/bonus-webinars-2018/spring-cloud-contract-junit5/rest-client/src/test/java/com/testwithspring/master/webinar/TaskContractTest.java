package com.testwithspring.master.webinar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test ensures that the contract provided
 * by the REST service has not changed.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "com.testwithspring.master.webinar:contract-rest-service-junit5:0.0.1-SNAPSHOT:stubs:9090"
)
public class TaskContractTest {

    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        restTemplate = new RestTemplate();
    }

    @Test
    @DisplayName("Should return the HTTP status code 200")
    public void shouldReturnHttpStatusCodeOk() {
        ResponseEntity<Task> taskResponse = restTemplate.getForEntity("http://localhost:9090/api/task/1", Task.class);
        assertThat(taskResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should return a task that has the correct id")
    public void shouldReturnTaskWithCorrectId() {
        ResponseEntity<Task> taskResponse = restTemplate.getForEntity("http://localhost:9090/api/task/1", Task.class);

        Task task = taskResponse.getBody();
        assertThat(task.getId()).isEqualByComparingTo(1L);
    }

    @Test
    @DisplayName("Should return a task that has the correct title")
    public void shouldReturnTaskWithCorrectTitle() {
        ResponseEntity<Task> taskResponse = restTemplate.getForEntity("http://localhost:9090/api/task/1", Task.class);

        Task task = taskResponse.getBody();
        assertThat(task.getTitle()).isEqualTo("Write our first contract");
    }
}
