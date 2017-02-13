
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

import static org.hamcrest.Matchers.is;
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
public class ProcessUpdateTaskFormAsUserWhenValidationIsSuccessfulTest {

    private static final String FEEDBACK_MESSAGE_TASK_UPDATED = "The information of a task was updated successfully.";

    private static final String NEW_DESCRIPTION = "The old lesson was not good";
    private static final String NEW_TITLE = "Rewrite an existing lesson";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldReturnHttpStatusCodeFound() throws Exception {
        submitUpdateTaskForm()
                .andExpect(status().isFound());
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldRedirectUserToViewTaskView() throws Exception {
        submitUpdateTaskForm()
                .andExpect(view().name(WebTestConstants.RedirectView.SHOW_TASK))
                .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_ID, is(Tasks.WriteLesson.ID.toString())));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldRedirectUserToViewTaskPageUrl() throws Exception {
        submitUpdateTaskForm()
                .andExpect(redirectedUrl("/task/2"));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldAddFeedbackMessageAsAFlashAttribute() throws Exception {
        submitUpdateTaskForm()
                .andExpect(flash().attribute(WebTestConstants.FlashMessageKey.FEEDBACK_MESSAGE,
                        FEEDBACK_MESSAGE_TASK_UPDATED
                ));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "update-task-should-update-title-and-description.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldUpdateTitleAndDescription() throws Exception {
        submitUpdateTaskForm();
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "update-task-as-user-should-update-lifecycle-fields.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldUpdateModificationInformationAndVersion() throws Exception {
        submitUpdateTaskForm();
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "update-task-should-not-change-status.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldNotChangeStatus() throws Exception {
        submitUpdateTaskForm();
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "update-task-should-not-change-assignee.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldNotChangeAssignee() throws Exception {
        submitUpdateTaskForm();
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "update-task-should-not-change-tags.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldNotMakeAnyChangesToTagsOfUpdatedTask() throws Exception {
        submitUpdateTaskForm();
    }

    private ResultActions submitUpdateTaskForm() throws Exception {
        return  mockMvc.perform(post("/task/{taskId}/update", Tasks.WriteLesson.ID)
                .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, NEW_DESCRIPTION)
                .param(WebTestConstants.ModelAttributeProperty.Task.ID, Tasks.WriteLesson.ID.toString())
                .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, NEW_TITLE)
                .with(csrf())
        );
    }
}
