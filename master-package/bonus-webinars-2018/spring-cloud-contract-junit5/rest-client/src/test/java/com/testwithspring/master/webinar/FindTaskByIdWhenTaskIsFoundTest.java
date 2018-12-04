package com.testwithspring.master.webinar;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This test ensures that our REST client is working as expected
 * when the requested task is found.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "com.testwithspring.master.webinar:contract-rest-service-junit5:0.0.1-SNAPSHOT:stubs:8080"
)
public class FindTaskByIdWhenTaskIsFoundTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return the HTTP status code OK")
    public void shouldReturnHttpStatusCodeOk() throws Exception {
        mockMvc.perform(get("/api/task/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return the found task as JSON")
    public void shouldReturnFoundTaskAsJson() throws Exception {
        mockMvc.perform(get("/api/task/{id}", 1L))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @DisplayName("Should return the information of the correct task")
    public void shouldReturnInformationOfCorrectTask() throws Exception {
        mockMvc.perform(get("/api/task/{id}", 1L))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Write our first contract")));
    }
}
