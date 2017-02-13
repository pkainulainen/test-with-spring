package com.testwithspring.intermediate.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
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
public class ShowTaskListAsAdminWhenTwoTasksAreFoundTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    public void shouldReturnHttpStatusCodeOk() throws Exception {
        openTaskListPage()
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    public void shouldRenderTaskListView() throws Exception {
        openTaskListPage()
                .andExpect(view().name(WebTestConstants.View.TASK_LIST));
    }

    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    public void shouldShowCorrectTasks() throws Exception {
        openTaskListPage()
                .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, allOf(
                        hasItem(allOf(
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(Tasks.WriteExampleApp.ID)),
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(Tasks.WriteExampleApp.TITLE)),
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.STATUS, is(Tasks.WriteExampleApp.STATUS))
                        )),
                        hasItem(allOf(
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(Tasks.WriteLesson.ID)),
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(Tasks.WriteLesson.TITLE)),
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.STATUS, is(Tasks.WriteLesson.STATUS))
                        ))
                )));
    }

    private ResultActions openTaskListPage() throws Exception {
        return mockMvc.perform(get("/"));
    }
}
