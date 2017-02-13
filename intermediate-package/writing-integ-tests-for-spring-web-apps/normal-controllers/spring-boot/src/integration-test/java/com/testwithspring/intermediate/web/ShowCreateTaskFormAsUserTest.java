package com.testwithspring.intermediate.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.testwithspring.intermediate.IntegrationTest;
import com.testwithspring.intermediate.IntegrationTestContext;
import com.testwithspring.intermediate.ReplacementDataSetLoader;
import com.testwithspring.intermediate.Users;
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
        "/com/testwithspring/intermediate/no-tasks-and-tags.xml"
})
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
public class ShowCreateTaskFormAsUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldReturnHttpStatusCodeOk() throws Exception {
        openCreateTaskPage()
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldRenderCreateNewTaskView() throws Exception {
        openCreateTaskPage()
                .andExpect(view().name(WebTestConstants.View.CREATE_TASK));
    }


    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldShowEmptyCreateTaskForm() throws Exception {
        openCreateTaskPage()
                .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, nullValue()),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, nullValue()),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, nullValue())
                )));
    }

    private ResultActions openCreateTaskPage() throws Exception {
        return mockMvc.perform(get("/task/create"));
    }
}
