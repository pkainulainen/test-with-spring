package com.testwithspring.intermediate.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.testwithspring.intermediate.*;
import com.testwithspring.intermediate.config.Profiles;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {IntegrationTestContext.class})
//@ContextConfiguration(locations = {"classpath:integration-test-context.xml"})
@WebAppConfiguration
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
public class SearchTasksAsUserWhenOneTaskIsFound {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void configureSystemUnderTest() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldReturnHttpStatusCodeOk() throws Exception {
        search()
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldReturnSearchResultsAsJson() throws Exception {
        search()
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldReturnOneTask() throws Exception {
        search()
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldReturnInformationOfFoundTask() throws Exception {
        search()
                .andExpect(jsonPath("$[0].id", is(Tasks.WriteLesson.ID.intValue())))
                .andExpect(jsonPath("$[0].status", is(Tasks.WriteLesson.STATUS.toString())))
                .andExpect(jsonPath("$[0].title", is(Tasks.WriteLesson.TITLE)));
    }

    private ResultActions search() throws Exception {
        return mockMvc.perform(get("/api/task/search")
                .param(WebTestConstants.RequestParameter.SEARCH_TERM, Tasks.SEARCH_TERM_ONE_MATCH)
        );
    }
}
