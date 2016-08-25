package com.testwithspring.starter.springboot;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class HelloControllerTest {

    private MockMvc mockMvc;

    @Before
    public void configureSystemUnderTest() {
        mockMvc = MockMvcBuilders.standaloneSetup(new HelloController())
                .build();
    }

    public class SayHello {

        @Test
        public void shouldReturnStringHelloWorld() throws Exception {
            mockMvc.perform(get("/api/hello"))
                    .andExpect(content().string("Hello World!"));
        }
    }
}
