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
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {IntegrationTestContext.class})
//@ContextConfiguration(locations = {"classpath:integration-test-context.xml"})
@WebAppConfiguration
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        ServletTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class
})
@DatabaseSetup({
        "/com/testwithspring/intermediate/users.xml",
        "/com/testwithspring/intermediate/empty-database.xml"
})
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
public class ProcessCreateNewTaskFormAsUserWhenValidationIsSuccessfulTest {

    private static final String FEEDBACK_MESSAGE_TASK_CREATED = "A new task was created successfully.";

    @Autowired
    IdColumnReset idColumnReset;

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void configureSystemUnderTest() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build();

        idColumnReset.resetIdColumns("tasks");
    }

    @Test
    @WithUserDetails(Users.JohnDoe.USERNAME)
    public void shouldReturnHttpStatusCodeFound() throws Exception {
        submitCreateTaskForm()
                .andExpect(status().isFound());
    }

    @Test
    @WithUserDetails(Users.JohnDoe.USERNAME)
    public void shouldRedirectUserToViewTaskView() throws Exception {
        submitCreateTaskForm()
                .andExpect(view().name(WebTestConstants.RedirectView.SHOW_TASK))
                .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_ID, is("1")));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.USERNAME)
    public void shouldRedirectUserToViewTaskPageUrl() throws Exception {
        submitCreateTaskForm()
                .andExpect(redirectedUrl("/task/1"));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.USERNAME)
    public void shouldAddFeedbackMessageAsAFlashAttribute() throws Exception {
        submitCreateTaskForm()
                .andExpect(flash().attribute(WebTestConstants.FlashMessageKey.FEEDBACK_MESSAGE,
                        FEEDBACK_MESSAGE_TASK_CREATED
                ));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.USERNAME)
    @ExpectedDatabase(value = "create-task-should-create-open-task.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldCreateOpenTask() throws Exception {
        submitCreateTaskForm();
    }

    @Test
    @WithUserDetails(Users.JohnDoe.USERNAME)
    @ExpectedDatabase(value = "create-task-should-set-title-and-description.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldCreateTaskWithCorrectTitleAndDescription() throws Exception {
        submitCreateTaskForm();
    }

    @Test
    @WithUserDetails(Users.JohnDoe.USERNAME)
    @ExpectedDatabase(value = "create-task-as-user-should-set-lifecycle-field-values.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldCreateTaskWithCorrectLifecycleFieldValues() throws Exception {
        submitCreateTaskForm();
    }

    @Test
    @WithUserDetails(Users.JohnDoe.USERNAME)
    @ExpectedDatabase(value = "create-task-should-create-unassigned-task.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldCreateTaskThatIsNotAssignedToAnyone() throws Exception {
        submitCreateTaskForm();
    }

    @Test
    @WithUserDetails(Users.JohnDoe.USERNAME)
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
