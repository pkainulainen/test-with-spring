package com.testwithspring.master.web;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;
import com.testwithspring.master.IntegrationTestContext;
import com.testwithspring.master.Tasks;
import com.testwithspring.master.Users;
import com.testwithspring.master.WebIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebIntegrationTest(classes = IntegrationTestContext.class)
@DatabaseSetup({
        "/com/testwithspring/master/users.xml",
        "/com/testwithspring/master/tasks.xml"
})
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@DisplayName("Find task as a user when the requested task is found")
public class FindTaskAsUserWhenTaskIsFoundTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @DisplayName("Should return the HTTP status code OK ")
    void shouldReturnHttpStatusCodeOk() throws Exception {
        findTask()
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @DisplayName("Should return the found task as JSON")
    void shouldReturnFoundTaskAsJson() throws Exception {
        findTask()
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @DisplayName("Should return the information of the found task")
    void shouldReturnInformationOfFoundTask() throws Exception {
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
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @DisplayName("Should return a task that has one tag")
    void shouldReturnTaskThatHasOneTag() throws Exception {
        findTask()
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TAGS, hasSize(1)));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @DisplayName("Should return the information of the correct tag")
    void shouldReturnInformationOfCorrectTag() throws Exception {
        findTask()
                .andExpect(jsonPath("$.tags[0].id", is(Tasks.WriteExampleApp.Tags.Example.ID.intValue())))
                .andExpect(jsonPath("$.tags[0].name", is(Tasks.WriteExampleApp.Tags.Example.NAME)));
    }


    private ResultActions findTask() throws Exception {
        return mockMvc.perform(get("/api/task/{taskId}", Tasks.WriteExampleApp.ID));
    }
}
