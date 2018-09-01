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
@DisplayName("Find task as a user when the requested task is not found")
public class FindTaskAsUserWhenTaskIsNotFoundTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @DisplayName("Should return the HTTP status code not found")
    void shouldReturnHttpStatusCodeNotFound() throws Exception {
        mockMvc.perform(get("/api/task/{taskId}", Tasks.TASK_ID_NOT_FOUND))
                .andExpect(status().isNotFound());
    }

    private ResultActions findTask() throws Exception {
        return mockMvc.perform(get("/api/task/{taskId}", Tasks.WriteExampleApp.ID));
    }
}
