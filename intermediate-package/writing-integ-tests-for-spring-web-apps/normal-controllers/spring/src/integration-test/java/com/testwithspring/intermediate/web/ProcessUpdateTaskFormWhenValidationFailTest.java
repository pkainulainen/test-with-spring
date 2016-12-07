package com.testwithspring.intermediate.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
@DatabaseSetup("task.xml")
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
public class ProcessUpdateTaskFormWhenValidationFailTest {

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
        submitEmptyUpdateTaskForm()
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRenderUpdateTaskView() throws Exception {
        submitEmptyUpdateTaskForm()
                .andExpect(view().name(WebTestConstants.View.UPDATE_TASK));
    }

    @Test
    public void shouldForwardUserToUpdateTaskPageUrl() throws Exception {
        submitEmptyUpdateTaskForm()
                .andExpect(forwardedUrl("/WEB-INF/jsp/task/update.jsp"));
    }

    @Test
    public void shouldShowValidationErrorForEmptyTitle() throws Exception {
        submitEmptyUpdateTaskForm()
                .andExpect(model().attributeHasFieldErrorCode(WebTestConstants.ModelAttributeName.TASK,
                        TASK_PROPERTY_NAME_TITLE,
                        is(WebTestConstants.ValidationErrorCode.EMPTY_FIELD)
                ));
    }


    @Test
    public void shouldShowFieldValuesOfCreateTaskForm() throws Exception {
        submitEmptyUpdateTaskForm()
                .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                        hasProperty(TASK_PROPERTY_NAME_DESCRIPTION, is("")),
                        hasProperty(TASK_PROPERTY_NAME_TITLE, is(""))
                )));
    }

    @Test
    public void shouldNotModifyHiddenIdParameter() throws Exception {
        submitEmptyUpdateTaskForm()
                .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                        hasProperty(TASK_PROPERTY_NAME_ID, is(Tasks.WriteLesson.ID))
                )));
    }

    @Test
    @ExpectedDatabase(value = "task.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void shouldNotUpdateTask() throws Exception {
        submitEmptyUpdateTaskForm();
    }

    private ResultActions submitEmptyUpdateTaskForm() throws Exception {
        return  mockMvc.perform(post("/task/{taskId}/update", Tasks.WriteLesson.ID)
                .param(TASK_PROPERTY_NAME_DESCRIPTION, "")
                .param(TASK_PROPERTY_NAME_ID, Tasks.WriteLesson.ID.toString())
                .param(TASK_PROPERTY_NAME_TITLE, "")
        );
    }
}
