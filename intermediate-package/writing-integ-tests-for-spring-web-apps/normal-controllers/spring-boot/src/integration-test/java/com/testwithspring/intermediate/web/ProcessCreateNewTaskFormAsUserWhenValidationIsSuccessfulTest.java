package com.testwithspring.intermediate.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.testwithspring.intermediate.*;
import com.testwithspring.intermediate.config.Profiles;
import org.junit.Before;
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
        "/com/testwithspring/intermediate/no-tasks-and-tags.xml"
})
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
public class ProcessCreateNewTaskFormAsUserWhenValidationIsSuccessfulTest {

    private static final String FEEDBACK_MESSAGE_TASK_CREATED = "A new task was created successfully.";

    @Autowired
    IdColumnReset idColumnReset;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void configureSystemUnderTest() {
        idColumnReset.resetIdColumns("tasks");
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldReturnHttpStatusCodeFound() throws Exception {
        submitCreateTaskForm()
                .andExpect(status().isFound());
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldRedirectUserToViewTaskView() throws Exception {
        submitCreateTaskForm()
                .andExpect(view().name(WebTestConstants.RedirectView.SHOW_TASK))
                .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_ID, is("1")));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldRedirectUserToViewTaskPageUrl() throws Exception {
        submitCreateTaskForm()
                .andExpect(redirectedUrl("/task/1"));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldAddFeedbackMessageAsAFlashAttribute() throws Exception {
        submitCreateTaskForm()
                .andExpect(flash().attribute(WebTestConstants.FlashMessageKey.FEEDBACK_MESSAGE,
                        FEEDBACK_MESSAGE_TASK_CREATED
                ));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "create-task-should-create-open-task.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldCreateOpenTask() throws Exception {
        submitCreateTaskForm();
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "create-task-should-set-title-and-description.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldCreateTaskWithCorrectTitleAndDescription() throws Exception {
        submitCreateTaskForm();
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "create-task-as-user-should-set-lifecycle-field-values.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldCreateTaskWithCorrectLifecycleFieldValues() throws Exception {
        submitCreateTaskForm();
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "create-task-should-create-unassigned-task.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldCreateTaskThatIsNotAssignedToAnyone() throws Exception {
        submitCreateTaskForm();
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "create-task-should-not-create-any-tags.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldNotCreateAnyTags() throws Exception {
        submitCreateTaskForm();
    }

    private ResultActions submitCreateTaskForm() throws Exception {
        return  mockMvc.perform(post("/task/create")
                .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, Tasks.WriteExampleApp.DESCRIPTION)
                .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, Tasks.WriteExampleApp.TITLE)
                .with(csrf())
        );
    }
}
