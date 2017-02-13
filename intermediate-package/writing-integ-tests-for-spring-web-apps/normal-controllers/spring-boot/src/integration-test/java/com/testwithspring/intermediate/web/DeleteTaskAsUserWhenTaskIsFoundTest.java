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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class DeleteTaskAsUserWhenTaskIsFoundTest {

    private static final String FEEDBACK_MESSAGE_TASK_DELETED = "Task was deleted successfully.";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldReturnHttpResponseStatusFound() throws Exception {
        deleteTask()
                .andExpect(status().isFound());
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldRedirectUserToViewTaskListView() throws Exception {
        deleteTask()
                .andExpect(view().name(WebTestConstants.RedirectView.SHOW_TASK_LIST));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldRedirectUserToViewTaskListPageUrl() throws Exception {
        deleteTask()
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldAddFeedbackMessageAsAFlashAttribute() throws Exception {
        deleteTask()
                .andExpect(flash().attribute(WebTestConstants.FlashMessageKey.FEEDBACK_MESSAGE,
                        FEEDBACK_MESSAGE_TASK_DELETED
                ));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "delete-task-should-delete-correct-task.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldDeleteCorrectTask() throws Exception {
        deleteTask();
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "delete-task-should-delete-link-between-tag-and-deleted-task.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldDeleteLinkBetweenTagAndDeletedTask() throws Exception {
        deleteTask();
    }


    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "delete-task-should-not-delete-tags.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldNotDeleteTags() throws Exception {
        deleteTask();
    }

    private ResultActions deleteTask() throws Exception {
        return  mockMvc.perform(get("/task/{taskId}/delete", Tasks.WriteLesson.ID)
                .with(csrf())
        );
    }
}
