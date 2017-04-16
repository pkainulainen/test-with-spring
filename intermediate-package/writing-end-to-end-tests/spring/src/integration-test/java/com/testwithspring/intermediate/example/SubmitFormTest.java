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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ExampleApplicationContext.class})
//@ContextConfiguration(locations = {"classpath:example-context.xml"})
@WebAppConfiguration
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@Category(IntegrationTest.class)
public class SubmitFormTest {

    private static final String MESSAGE = "Hello";
    private static final Integer NUMBER = 42;
    private static final String NUMBER_STRING = "42";
    private static final String TRUE = "true";

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
        submitForm()
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRenderFormResultView() throws Exception {
        submitForm()
                .andExpect(view().name(WebTestConstants.Views.FORM_RESULT_VIEW));
    }

    @Test
    public void shouldForwardUserToFormResultPage() throws Exception {
        submitForm()
                .andExpect(forwardedUrl("/WEB-INF/jsp/form/result.jsp"));
    }

    @Test
    public void shouldRenderFormResultViewWithCorrectInformation() throws Exception {
        submitForm()
                .andExpect(model().attribute(WebTestConstants.ModelAttributeNames.FORM, allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperties.Form.MESSAGE, is(MESSAGE)),
                        hasProperty(WebTestConstants.ModelAttributeProperties.Form.NUMBER, is(NUMBER)),
                        hasProperty(WebTestConstants.ModelAttributeProperties.Form.CHECKBOX, is(true)),
                        hasProperty(WebTestConstants.ModelAttributeProperties.Form.RADIO_BUTTON, is(true))
                )));
    }

    private ResultActions submitForm() throws Exception {
        return mockMvc.perform(post("/form")
                .param(WebTestConstants.ModelAttributeProperties.Form.MESSAGE, MESSAGE)
                .param(WebTestConstants.ModelAttributeProperties.Form.NUMBER, NUMBER_STRING)
                .param(WebTestConstants.ModelAttributeProperties.Form.CHECKBOX, TRUE)
                .param(WebTestConstants.ModelAttributeProperties.Form.RADIO_BUTTON, TRUE)
        );
    }
}
