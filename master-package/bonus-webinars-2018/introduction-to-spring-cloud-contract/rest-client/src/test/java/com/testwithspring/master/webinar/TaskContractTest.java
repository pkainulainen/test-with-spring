package com.testwithspring.master.webinar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test ensures that the contract provided
 * by the REST service has not changed.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "com.testwithspring.master.webinar:contract-rest-service:0.0.1-SNAPSHOT:stubs:9090"
)
public class TaskContractTest {

    private RestTemplate restTemplate;

    @Before
    public void setup() {
        restTemplate = new RestTemplate();
    }

    @Test
    public void shouldReturnHttpStatusCodeOk() {
        ResponseEntity<Task> taskResponse = restTemplate.getForEntity("http://localhost:9090/api/task/1", Task.class);
        assertThat(taskResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnTaskWithCorrectId() {
        ResponseEntity<Task> taskResponse = restTemplate.getForEntity("http://localhost:9090/api/task/1", Task.class);

        Task task = taskResponse.getBody();
        assertThat(task.getId()).isEqualByComparingTo(1L);
    }

    @Test
    public void shouldReturnTaskWithCorrectTitle() {
        ResponseEntity<Task> taskResponse = restTemplate.getForEntity("http://localhost:9090/api/task/1", Task.class);

        Task task = taskResponse.getBody();
        assertThat(task.getTitle()).isEqualTo("Write our first contract");
    }
}
