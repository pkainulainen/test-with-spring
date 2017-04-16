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

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class FormExampleControllerTest {

    private MockMvc mockMvc;

    @Before
    public void configureSystemUnderTest() {
        mockMvc = MockMvcBuilders.standaloneSetup(new FormExampleController())
                .setViewResolvers(WebTestConfig.viewResolver())
                .build();
    }

    public class RenderFormView {

        @Test
        public void shouldReturnHttpStatusCodeOk() throws Exception {
            renderFormView()
                    .andExpect(status().isOk());
        }

        @Test
        public void shouldRenderFormView() throws Exception {
            renderFormView()
                    .andExpect(view().name(WebTestConstants.Views.FORM_VIEW));
        }

        @Test
        public void shouldRenderFormViewWithEmptyForm() throws Exception {
            renderFormView()
                    .andExpect(model().attribute(WebTestConstants.ModelAttributeNames.FORM, allOf(
                            hasProperty(WebTestConstants.ModelAttributeProperties.Form.MESSAGE, nullValue()),
                            hasProperty(WebTestConstants.ModelAttributeProperties.Form.NUMBER, nullValue()),
                            hasProperty(WebTestConstants.ModelAttributeProperties.Form.CHECKBOX, is(false)),
                            hasProperty(WebTestConstants.ModelAttributeProperties.Form.RADIO_BUTTON, is(false))
                    )));
        }

        private ResultActions renderFormView() throws Exception {
            return mockMvc.perform(get("/form"));
        }
    }

    public class SubmitForm {

        private final String MESSAGE = "Hello";
        private final Integer NUMBER = 42;
        private final String NUMBER_STRING = "42";
        private final String TRUE = "true";

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
}
