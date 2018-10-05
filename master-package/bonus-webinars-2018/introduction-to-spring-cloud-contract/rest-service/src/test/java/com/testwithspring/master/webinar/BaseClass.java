package com.testwithspring.master.webinar;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

/**
 * This base class configures the system under test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaskServiceApplication.class)
public abstract class BaseClass {

    @Autowired
    private TaskController controller;

    @MockBean
    private TaskService service;

    /**
     * This setup method ensures that our REST API fulfills
     * our contract.
     */
    @Before
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(controller);
        when(service.findById(1L)).thenReturn(new Task(1L, "Write our first contract"));
    }
}
