
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
public class ProcessUpdateTaskFormAsAdminWhenTaskIsNotFoundTest {

    private static final String NEW_DESCRIPTION = "The old lesson was not good";
    private static final String NEW_TITLE = "Rewrite an existing lesson";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    public void shouldReturnHttpStatusCodeNotFound() throws Exception {
        submitUpdateTaskForm()
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    public void shouldRenderNotFoundView() throws Exception {
        submitUpdateTaskForm()
                .andExpect(view().name(WebTestConstants.ErrorView.NOT_FOUND));
    }

    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    @ExpectedDatabase(value = "task.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldNotUpdateTheInformationOfTask() throws Exception {
        submitUpdateTaskForm();
    }

    private ResultActions submitUpdateTaskForm() throws Exception {
        return  mockMvc.perform(post("/task/{taskId}/update", Tasks.TASK_ID_NOT_FOUND)
                .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, NEW_DESCRIPTION)
                .param(WebTestConstants.ModelAttributeProperty.Task.ID, Tasks.TASK_ID_NOT_FOUND.toString())
                .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, NEW_TITLE)
                .with(csrf())
        );
    }
}
