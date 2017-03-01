package com.testwithspring.intermediate.message;

import com.testwithspring.intermediate.IntegrationTest;
import com.testwithspring.intermediate.config.ExampleApplicationContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ExampleApplicationContext.class})
//@ContextConfiguration(locations = {"classpath:example-context.xml"})
@WebAppConfiguration
@Category(IntegrationTest.class)
public class ShowMessageTest {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void configureSystemUnderTest() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .build();
    }

    @Test
    public void shouldReturnHttpStatusCodeOk() throws Exception {
        showMessage()
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRenderShowMessageView() throws Exception {
        showMessage()
                .andExpect(view().name("index"));
    }

    @Test
    public void shouldForwardUserToShowMessagePage() throws Exception {
        showMessage()
                .andExpect(forwardedUrl("/WEB-INF/jsp/index.jsp"));
    }

    private ResultActions showMessage() throws Exception {
        return mockMvc.perform(get("/"));
    }
}
