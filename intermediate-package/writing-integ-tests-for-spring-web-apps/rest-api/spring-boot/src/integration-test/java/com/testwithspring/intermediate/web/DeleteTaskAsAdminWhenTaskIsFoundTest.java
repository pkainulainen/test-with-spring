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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
        "/com/testwithspring/intermediate/tasks.xml"
})
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
public class DeleteTaskAsAdminWhenTaskIsFoundTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    public void shouldReturnHttpStatusCodeOk() throws Exception {
        deleteTask()
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    public void shouldReturnDeletedTaskAsJson() throws Exception {
        deleteTask()
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    public void shouldReturnInformationOfDeletedTask() throws Exception {
        deleteTask()
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ASSIGNEE, nullValue()))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CLOSER, nullValue()))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CREATION_TIME, is(Tasks.WriteLesson.CREATION_TIME_STRING)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(Tasks.WriteLesson.Creator.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(Tasks.WriteLesson.Creator.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(Tasks.WriteLesson.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.MODIFICATION_TIME, is(Tasks.WriteLesson.MODIFICATION_TIME_STRING)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(Tasks.WriteLesson.Modifier.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(Tasks.WriteLesson.Modifier.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(Tasks.WriteLesson.TITLE)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, is(Tasks.WriteLesson.DESCRIPTION)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(Tasks.WriteLesson.STATUS.toString())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, isEmptyOrNullString()));
    }

    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    public void shouldReturnTaskThatHasOneTag() throws Exception {
        deleteTask()
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TAGS, hasSize(1)));
    }

    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    public void shouldReturnInformationOfCorrectTag() throws Exception {
        deleteTask()
                .andExpect(jsonPath("$.tags[0].id", is(Tasks.WriteLesson.Tags.Lesson.ID.intValue())))
                .andExpect(jsonPath("$.tags[0].name", is(Tasks.WriteLesson.Tags.Lesson.NAME)));
    }

    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "delete-task-should-delete-correct-task.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldDeleteCorrectTask() throws Exception {
        deleteTask();
    }

    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "delete-task-should-delete-link-between-tag-and-deleted-task.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldDeleteLinkBetweenTagAndDeletedTask() throws Exception {
        deleteTask();
    }


    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "delete-task-should-not-delete-tags.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldNotDeleteTags() throws Exception {
        deleteTask();
    }

    private ResultActions deleteTask() throws Exception {
        return  mockMvc.perform(delete("/api/task/{taskId}", Tasks.WriteLesson.ID)
                .with(csrf())
        );
    }
}
