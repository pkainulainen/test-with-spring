package com.testwithspring.master.web;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;
import com.testwithspring.master.IntegrationTestContext;
import com.testwithspring.master.Tasks;
import com.testwithspring.master.WebIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebIntegrationTest(classes = IntegrationTestContext.class)
@DatabaseSetup({
        "/com/testwithspring/master/users.xml",
        "/com/testwithspring/master/tasks.xml"
})
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@DisplayName("Find one task as an anonymous user")
class FindTaskAsAnonymousUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return the HTTP status code unauthorized")
    void shouldReturnHttpStatusCodeUnauthorized() throws Exception {
        findTask()
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return an empty response body")
    void shouldReturnEmptyResponseBody() throws Exception {
        findTask()
                .andExpect(content().string(""));
    }

    private ResultActions findTask() throws Exception {
        return mockMvc.perform(get("/api/task/{taskId}", Tasks.WriteExampleApp.ID));
    }
}
