package com.testwithspring.intermediate.example;

import com.testwithspring.intermediate.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ExampleApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@Category(IntegrationTest.class)
public class RenderFormViewTest {

    @Autowired
    private MockMvc mockMvc;

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
