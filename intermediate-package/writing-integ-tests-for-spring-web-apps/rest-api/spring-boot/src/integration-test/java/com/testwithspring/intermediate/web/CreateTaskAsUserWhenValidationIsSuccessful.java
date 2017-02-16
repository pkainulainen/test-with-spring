package com.testwithspring.intermediate.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.testwithspring.intermediate.*;
import com.testwithspring.intermediate.common.ConstantDateTimeService;
import com.testwithspring.intermediate.config.Profiles;
import com.testwithspring.intermediate.task.TaskFormDTO;
import com.testwithspring.intermediate.task.TaskStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
        "/com/testwithspring/intermediate/no-tasks-and-tags.xml"
})
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
public class CreateTaskAsUserWhenValidationIsSuccessful {

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
    public void shouldReturnHttpStatusCodeCreated() throws Exception {
        createTask()
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldReturnCreatedTaskAsJson() throws Exception {
        createTask()
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldReturnInformationOfCreatedTask() throws Exception {
        createTask()
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ASSIGNEE, nullValue()))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CLOSER, nullValue()))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CREATION_TIME, is(ConstantDateTimeService.CURRENT_DATE_AND_TIME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(Users.JohnDoe.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(Users.JohnDoe.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(Tasks.WriteExampleApp.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.MODIFICATION_TIME, is(ConstantDateTimeService.CURRENT_DATE_AND_TIME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(Users.JohnDoe.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(Users.JohnDoe.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(Tasks.WriteExampleApp.TITLE)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, is(Tasks.WriteExampleApp.DESCRIPTION)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(TaskStatus.OPEN.toString())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, nullValue()));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldReturnTaskThatHasZeroTags() throws Exception {
        createTask()
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TAGS, hasSize(0)));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "create-task-should-create-open-task.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldCreateOpenTask() throws Exception {
        createTask();
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "create-task-should-set-title-and-description.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldCreateTaskWithCorrectTitleAndDescription() throws Exception {
        createTask();
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "create-task-as-user-should-set-lifecycle-field-values.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldCreateTaskWithCorrectLifecycleFieldValues() throws Exception {
        createTask();
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "create-task-should-create-unassigned-task.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldCreateTaskThatIsNotAssignedToAnyone() throws Exception {
        createTask();
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "create-task-should-not-create-any-tags.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldNotCreateAnyTags() throws Exception {
        createTask();
    }

    private ResultActions createTask() throws Exception {
        TaskFormDTO input = new TaskFormDTO();
        input.setDescription(Tasks.WriteExampleApp.DESCRIPTION);
        input.setTitle(Tasks.WriteExampleApp.TITLE);

        return mockMvc.perform(post("/api/task")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(WebTestUtil.convertObjectToJsonBytes(input))
                .with(csrf())
        );
    }
}
