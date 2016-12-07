package com.testwithspring.intermediate.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.testwithspring.intermediate.IntegrationTest;
import com.testwithspring.intermediate.IntegrationTestContext;
import com.testwithspring.intermediate.ReplacementDataSetLoader;
import com.testwithspring.intermediate.config.Profiles;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {IntegrationTestContext.class})
//@ContextConfiguration(locations = {"classpath:integration-test-context.xml"})
@WebAppConfiguration
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        ServletTestExecutionListener.class
})
@DatabaseSetup("/com/testwithspring/intermediate/empty-database.xml")
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
public class ShowCreateTaskFormTest {

    private static final String TASK_PROPERTY_NAME_DESCRIPTION = "description";
    private static final String TASK_PROPERTY_NAME_ID = "id";
    private static final String TASK_PROPERTY_NAME_TITLE = "title";

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void configureSystemUnderTest() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .build();
    }

    @Test
    public void shouldReturnHttpStatusCodeOk() throws Exception {
        openCreateTaskPage()
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRenderCreateNewTaskView() throws Exception {
        openCreateTaskPage()
                .andExpect(view().name(WebTestConstants.View.CREATE_TASK));
    }

    @Test
    public void shouldForwardUserToCreateNewTaskPageUrl() throws Exception {
        openCreateTaskPage()
                .andExpect(forwardedUrl("/WEB-INF/jsp/task/create.jsp"));
    }

    @Test
    public void shouldShowEmptyCreateTaskForm() throws Exception {
        openCreateTaskPage()
                .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                        hasProperty(TASK_PROPERTY_NAME_DESCRIPTION, nullValue()),
                        hasProperty(TASK_PROPERTY_NAME_ID, nullValue()),
                        hasProperty(TASK_PROPERTY_NAME_TITLE, nullValue())
                )));
    }

    private ResultActions openCreateTaskPage() throws Exception {
        return mockMvc.perform(get("/task/create"));
    }
}
