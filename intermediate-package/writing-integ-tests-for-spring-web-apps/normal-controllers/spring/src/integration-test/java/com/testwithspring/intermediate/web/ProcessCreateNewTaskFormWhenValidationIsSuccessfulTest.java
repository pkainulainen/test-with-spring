package com.testwithspring.intermediate.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.testwithspring.intermediate.IdColumnReset;
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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
public class ProcessCreateNewTaskFormWhenValidationIsSuccessfulTest {

    private static final String FEEDBACK_MESSAGE_TASK_CREATED = "A new task was created successfully.";

    @Autowired
    IdColumnReset sequenceResetor;

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void configureSystemUnderTest() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .build();

        sequenceResetor.resetIdColumnSequences("tasks");
    }

    @Test
    public void shouldReturnHttpStatusCodeFound() throws Exception {
        submitCreateTaskForm()
                .andExpect(status().isFound());
    }

    @Test
    public void shouldRedirectUserToViewTaskView() throws Exception {
        submitCreateTaskForm()
                .andExpect(view().name(WebTestConstants.RedirectView.SHOW_TASK))
                .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_ID, is("1")));
    }

    @Test
    public void shouldRedirectUserToViewTaskPageUrl() throws Exception {
        submitCreateTaskForm()
                .andExpect(redirectedUrl("/task/1"));
    }

    @Test
    public void shouldAddFeedbackMessageAsAFlashAttribute() throws Exception {
        submitCreateTaskForm()
                .andExpect(flash().attribute(WebTestConstants.FlashMessageKey.FEEDBACK_MESSAGE,
                        FEEDBACK_MESSAGE_TASK_CREATED
                ));
    }

    @Test
    @ExpectedDatabase(value = "create-new-task-should-create-new-task.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldCreateOpenTask() throws Exception {
        submitCreateTaskForm();
    }

    private ResultActions submitCreateTaskForm() throws Exception {
        return  mockMvc.perform(post("/task/create")
                .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, Tasks.WriteExampleApp.DESCRIPTION)
                .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, Tasks.WriteExampleApp.TITLE)
        );
    }
}
