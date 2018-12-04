package com.testwithspring.master.webinar;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * This base class configures the system under test.
 */
public abstract class BaseTest {

    /**
     * This setup method ensures that our REST API fulfills
     * our contract.
     */
    @Before
    public void setup() {
        TaskService service = mock(TaskService.class);

        RestAssuredMockMvc.standaloneSetup(new TaskController(service));
        given(service.findById(1L)).willReturn(new Task(1L, "Write our first contract"));
    }
}
