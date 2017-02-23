package com.testwithspring.intermediate.web;

import com.testwithspring.intermediate.UnitTest;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class LoginControllerTest {

    private MockMvc mockMvc;

    @Before
    public void configureSystemUnderTest() {
        mockMvc = MockMvcBuilders.standaloneSetup(new LoginController())
                .build();
    }

    public class ShowLoginPage {

        @Test
        public void shouldReturnHttpStatusCodeOk() throws Exception {
            mockMvc.perform(get("/user/login"))
                    .andExpect(status().isOk());
        }

        @Test
        public void shouldRenderLoginView() throws Exception {
            mockMvc.perform(get("/user/login"))
                    .andExpect(view().name(WebTestConstants.View.LOGIN));
        }
    }
}
