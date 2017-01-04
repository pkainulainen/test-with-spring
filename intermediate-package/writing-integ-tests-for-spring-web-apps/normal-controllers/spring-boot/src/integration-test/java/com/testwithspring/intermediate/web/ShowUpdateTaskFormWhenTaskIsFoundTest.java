package com.testwithspring.intermediate.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.testwithspring.intermediate.IntegrationTest;
import com.testwithspring.intermediate.IntegrationTestContext;
import com.testwithspring.intermediate.ReplacementDataSetLoader;
import com.testwithspring.intermediate.Tasks;
import com.testwithspring.intermediate.config.Profiles;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {IntegrationTestContext.class})
@AutoConfigureMockMvc
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
public class ShowUpdateTaskFormWhenTaskIsFoundTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnHttpStatusCodeOk() throws Exception {
        openUpdateTaskPage()
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRenderUpdateTaskView() throws Exception {
        openUpdateTaskPage()
                .andExpect(view().name(WebTestConstants.View.UPDATE_TASK));
    }

    @Test
    public void shouldShowInformationOfUpdatedTask() throws Exception {
       openUpdateTaskPage()
                .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is(Tasks.WriteLesson.DESCRIPTION)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(Tasks.WriteLesson.ID)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(Tasks.WriteLesson.TITLE))
                )));
    }

    private ResultActions openUpdateTaskPage() throws Exception {
        return  mockMvc.perform(get("/task/{taskId}/update", Tasks.WriteLesson.ID));
    }
}
