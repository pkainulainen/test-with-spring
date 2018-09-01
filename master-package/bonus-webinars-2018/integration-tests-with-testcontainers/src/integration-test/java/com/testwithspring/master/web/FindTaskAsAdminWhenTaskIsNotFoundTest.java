package com.testwithspring.master.web;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;
import com.testwithspring.master.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebIntegrationTest(classes = IntegrationTestContext.class)
@DatabaseSetup({
        "/com/testwithspring/master/users.xml",
        "/com/testwithspring/master/tasks.xml"
})
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@DisplayName("Find task as an administrator when the requested task is not found")
public class FindTaskAsAdminWhenTaskIsNotFoundTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    @DisplayName("Should return the HTTP status code not found")
    void shouldReturnHttpStatusCodeNotFound() throws Exception {
        mockMvc.perform(get("/api/task/{taskId}", Tasks.TASK_ID_NOT_FOUND))
                .andExpect(status().isNotFound());
    }

    private ResultActions findTask() throws Exception {
        return mockMvc.perform(get("/api/task/{taskId}", Tasks.WriteExampleApp.ID));
    }
}
