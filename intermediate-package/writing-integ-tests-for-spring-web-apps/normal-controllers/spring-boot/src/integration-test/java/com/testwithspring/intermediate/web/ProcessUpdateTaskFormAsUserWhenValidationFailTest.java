package com.testwithspring.intermediate.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.testwithspring.intermediate.*;
import com.testwithspring.intermediate.config.Profiles;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {IntegrationTestContext.class})
@AutoConfigureMockMvc
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        ServletTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class
})
@DatabaseSetup({
        "/com/testwithspring/intermediate/users.xml",
        "task.xml"
})
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
public class ProcessUpdateTaskFormAsUserWhenValidationFailTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldReturnHttpStatusCodeOk() throws Exception {
        submitEmptyUpdateTaskForm()
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldRenderUpdateTaskView() throws Exception {
        submitEmptyUpdateTaskForm()
                .andExpect(view().name(WebTestConstants.View.UPDATE_TASK));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldShowValidationErrorForEmptyTitle() throws Exception {
        submitEmptyUpdateTaskForm()
                .andExpect(model().attributeHasFieldErrorCode(WebTestConstants.ModelAttributeName.TASK,
                        WebTestConstants.ModelAttributeProperty.Task.TITLE,
                        is(WebTestConstants.ValidationErrorCode.EMPTY_FIELD)
                ));
    }


    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldShowFieldValuesOfUpdateTaskForm() throws Exception {
        submitEmptyUpdateTaskForm()
                .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is("")),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(""))
                )));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldNotModifyHiddenIdParameter() throws Exception {
        submitEmptyUpdateTaskForm()
                .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(Tasks.WriteLesson.ID))
                )));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "task.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldNotUpdateTask() throws Exception {
        submitEmptyUpdateTaskForm();
    }

    private ResultActions submitEmptyUpdateTaskForm() throws Exception {
        return  mockMvc.perform(post("/task/{taskId}/update", Tasks.WriteLesson.ID)
                .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, "")
                .param(WebTestConstants.ModelAttributeProperty.Task.ID, Tasks.WriteLesson.ID.toString())
                .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, "")
                .with(csrf())
        );
    }
}
