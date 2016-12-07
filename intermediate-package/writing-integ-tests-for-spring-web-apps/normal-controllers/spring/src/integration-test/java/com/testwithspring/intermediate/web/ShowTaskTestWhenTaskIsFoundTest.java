package com.testwithspring.intermediate.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.testwithspring.intermediate.IntegrationTest;
import com.testwithspring.intermediate.IntegrationTestContext;
import com.testwithspring.intermediate.ReplacementDataSetLoader;
import com.testwithspring.intermediate.Tasks;
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
import static org.hamcrest.Matchers.is;
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
@DatabaseSetup("/com/testwithspring/intermediate/tasks.xml")
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
public class ShowTaskTestWhenTaskIsFoundTest {

    private static final String TASK_PROPERTY_NAME_ASSIGNEE = "assigneeId";
    private static final String TASK_PROPERTY_NAME_CLOSER = "closerId";
    private static final String TASK_PROPERTY_NAME_CREATION_TIME = "creationTime";
    private static final String TASK_PROPERTY_NAME_CREATOR = "creatorId";
    private static final String TASK_PROPERTY_NAME_DESCRIPTION = "description";
    private static final String TASK_PROPERTY_NAME_ID = "id";
    private static final String TASK_PROPERTY_NAME_MODIFICATION_TIME = "modificationTime";
    private static final String TASK_PROPERTY_NAME_RESOLUTION = "resolution";
    private static final String TASK_PROPERTY_NAME_STATUS = "status";
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
        openShowTaskPage()
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRenderShowTaskView() throws Exception {
        openShowTaskPage()
                .andExpect(view().name(WebTestConstants.View.VIEW_TASK));
    }

    @Test
    public void shouldForwardUserToShowTaskPageUrl() throws Exception {
        openShowTaskPage()
                .andExpect(forwardedUrl("/WEB-INF/jsp/task/view.jsp"));
    }

    @Test
    public void shouldShowFoundTask() throws Exception {
        mockMvc.perform(get("/task/{taskId}", Tasks.WriteExampleApp.ID))
                .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                        hasProperty(TASK_PROPERTY_NAME_ASSIGNEE, is(Tasks.WriteExampleApp.ASSIGNEE_ID)),
                        hasProperty(TASK_PROPERTY_NAME_CLOSER, is(Tasks.WriteExampleApp.CLOSER_ID)),
                        hasProperty(TASK_PROPERTY_NAME_CREATION_TIME, is(Tasks.WriteExampleApp.CREATION_TIME)),
                        hasProperty(TASK_PROPERTY_NAME_CREATOR, is(Tasks.WriteExampleApp.CREATOR_ID)),
                        hasProperty(TASK_PROPERTY_NAME_ID, is(Tasks.WriteExampleApp.ID)),
                        hasProperty(TASK_PROPERTY_NAME_MODIFICATION_TIME, is(Tasks.WriteExampleApp.MODIFICATION_TIME)),
                        hasProperty(TASK_PROPERTY_NAME_TITLE, is(Tasks.WriteExampleApp.TITLE)),
                        hasProperty(TASK_PROPERTY_NAME_DESCRIPTION, is(Tasks.WriteExampleApp.DESCRIPTION)),
                        hasProperty(TASK_PROPERTY_NAME_STATUS, is(Tasks.WriteExampleApp.STATUS)),
                        hasProperty(TASK_PROPERTY_NAME_RESOLUTION, is(Tasks.WriteExampleApp.RESOLUTION))
                )));
    }

    private ResultActions openShowTaskPage() throws Exception {
        return  mockMvc.perform(get("/task/{taskId}", Tasks.WriteExampleApp.ID));
    }
}
