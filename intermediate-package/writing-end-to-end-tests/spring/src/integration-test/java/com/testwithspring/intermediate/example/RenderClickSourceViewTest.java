package com.testwithspring.intermediate.example;

import com.testwithspring.intermediate.IntegrationTest;
import com.testwithspring.intermediate.config.ExampleApplicationContext;
import com.testwithspring.intermediate.config.Profiles;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@Category(IntegrationTest.class)
public class RenderClickSourceViewTest {

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
        renderClickSourceView()
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRenderClickSourceView() throws Exception {
        renderClickSourceView()
                .andExpect(view().name(WebTestConstants.Views.CLICK_SOURCE_VIEW));
    }

    @Test
    public void shouldForwardUserToClickSourcePage() throws Exception {
        renderClickSourceView()
                .andExpect(forwardedUrl("/WEB-INF/jsp/click/source.jsp"));
    }

    private ResultActions renderClickSourceView() throws Exception {
        return mockMvc.perform(get("/click-source"));
    }
}
