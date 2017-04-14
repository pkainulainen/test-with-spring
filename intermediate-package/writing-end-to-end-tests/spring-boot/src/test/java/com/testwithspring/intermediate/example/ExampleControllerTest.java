package com.testwithspring.intermediate.example;

import com.testwithspring.intermediate.UnitTest;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class ExampleControllerTest {

    private MockMvc mockMvc;

    @Before
    public void configureSystemUnderTest() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ExampleController())
                .setViewResolvers(WebTestConfig.viewResolver())
                .build();
    }

    public class RenderExampleView {

        @Test
        public void shouldReturnHttpStatusCodeOk() throws Exception {
            renderExampleView()
                    .andExpect(status().isOk());
        }

        @Test
        public void shouldRenderExampleView() throws Exception {
            renderExampleView()
                    .andExpect(view().name("index"));
        }

        private ResultActions renderExampleView() throws Exception {
            return mockMvc.perform(get("/"));
        }
    }
}
