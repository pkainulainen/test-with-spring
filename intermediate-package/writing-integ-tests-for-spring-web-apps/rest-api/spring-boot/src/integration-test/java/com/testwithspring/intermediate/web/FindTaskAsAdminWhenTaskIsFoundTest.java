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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
public class FindTaskAsAdminWhenTaskIsFoundTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    public void shouldReturnHttpStatusCodeOk() throws Exception {
        findTask()
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    public void shouldReturnFoundTaskAsJson() throws Exception {
        findTask()
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    public void shouldReturnInformationOfFoundTask() throws Exception {
        findTask()
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Assignee.ID, is(Tasks.WriteExampleApp.Assignee.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Assignee.NAME, is(Tasks.WriteExampleApp.Assignee.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Closer.ID, is(Tasks.WriteExampleApp.Closer.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Closer.NAME, is(Tasks.WriteExampleApp.Closer.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CREATION_TIME, is(Tasks.WriteExampleApp.CREATION_TIME_STRING)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(Tasks.WriteExampleApp.Creator.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(Tasks.WriteExampleApp.Creator.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(Tasks.WriteExampleApp.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.MODIFICATION_TIME, is(Tasks.WriteExampleApp.MODIFICATION_TIME_STRING)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(Tasks.WriteExampleApp.Modifier.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(Tasks.WriteExampleApp.Modifier.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(Tasks.WriteExampleApp.TITLE)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, is(Tasks.WriteExampleApp.DESCRIPTION)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(Tasks.WriteExampleApp.STATUS.toString())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, is(Tasks.WriteExampleApp.RESOLUTION.toString())));
    }

    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    public void shouldReturnTaskThatHasOneTag() throws Exception {
        findTask()
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TAGS, hasSize(1)));
    }

    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    public void shouldReturnInformationOfCorrectTag() throws Exception {
        findTask()
                .andExpect(jsonPath("$.tags[0].id", is(Tasks.WriteExampleApp.Tags.Example.ID.intValue())))
                .andExpect(jsonPath("$.tags[0].name", is(Tasks.WriteExampleApp.Tags.Example.NAME)));
    }

    private ResultActions findTask() throws Exception {
        return mockMvc.perform(get("/api/task/{taskId}", Tasks.WriteExampleApp.ID));
    }
}

