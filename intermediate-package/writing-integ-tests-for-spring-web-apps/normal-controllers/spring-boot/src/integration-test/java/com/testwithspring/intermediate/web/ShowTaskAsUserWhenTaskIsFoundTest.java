package com.testwithspring.intermediate.web;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.testwithspring.intermediate.*;
import com.testwithspring.intermediate.config.Profiles;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {IntegrationTestContext.class})
@AutoConfigureMockMvc
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
public class ShowTaskAsUserWhenTaskIsFoundTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldReturnHttpStatusCodeOk() throws Exception {
        openShowTaskPage()
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldRenderShowTaskView() throws Exception {
        openShowTaskPage()
                .andExpect(view().name(WebTestConstants.View.VIEW_TASK));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldShowFoundTask() throws Exception {
        openShowTaskPage()
                .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.ASSIGNEE, allOf(
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME,
                                        is(Tasks.WriteExampleApp.ASSIGNEE_NAME)
                                ),
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID,
                                        is(Tasks.WriteExampleApp.ASSIGNEE_ID)
                                )
                        )),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.CLOSER, allOf(
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME,
                                        is(Tasks.WriteExampleApp.CLOSER_NAME)
                                ),
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID,
                                        is(Tasks.WriteExampleApp.CLOSER_ID)
                                )
                        )),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.CREATION_TIME, is(Tasks.WriteExampleApp.CREATION_TIME)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.CREATOR, allOf(
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME,
                                        is(Tasks.WriteExampleApp.CREATOR_NAME)
                                ),
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID,
                                        is(Tasks.WriteExampleApp.CREATOR_ID)
                                )
                        )),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(Tasks.WriteExampleApp.ID)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.MODIFICATION_TIME, is(Tasks.WriteExampleApp.MODIFICATION_TIME)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.MODIFIER, allOf(
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME,
                                        is(Tasks.WriteExampleApp.MODIFIER_NAME)
                                ),
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID,
                                        is(Tasks.WriteExampleApp.MODIFIER_ID)
                                )
                        )),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(Tasks.WriteExampleApp.TITLE)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is(Tasks.WriteExampleApp.DESCRIPTION)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.STATUS, is(Tasks.WriteExampleApp.STATUS)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.RESOLUTION, is(Tasks.WriteExampleApp.RESOLUTION))
                )));
    }


    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldShowOneTagOfFoundTask() throws Exception {
        openShowTaskPage()
                .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.TAGS, hasSize(1))
                ));
    }

    @Test
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    public void shouldShowFoundTag() throws Exception {
        openShowTaskPage()
                .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.TAGS, contains(
                                allOf(
                                        hasProperty(
                                                WebTestConstants.ModelAttributeProperty.Tag.ID,
                                                is(Tasks.WriteExampleApp.Tags.Example.ID)
                                        ),
                                        hasProperty(
                                                WebTestConstants.ModelAttributeProperty.Tag.NAME,
                                                is(Tasks.WriteExampleApp.Tags.Example.NAME)
                                        )
                                )
                        ))
                ));
    }

    private ResultActions openShowTaskPage() throws Exception {
        return  mockMvc.perform(get("/task/{taskId}", Tasks.WriteExampleApp.ID));
    }
}
