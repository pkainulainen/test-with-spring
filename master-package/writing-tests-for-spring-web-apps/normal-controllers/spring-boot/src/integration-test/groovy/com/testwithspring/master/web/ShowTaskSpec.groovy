package com.testwithspring.master.web

import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader
import com.testwithspring.master.IntegrationTest
import com.testwithspring.master.IntegrationTestContext
import com.testwithspring.master.Tasks
import com.testwithspring.master.Users
import com.testwithspring.master.config.Profiles
import org.junit.experimental.categories.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import static org.hamcrest.Matchers.*

@SpringBootTest(classes = [IntegrationTestContext.class])
@AutoConfigureMockMvc
@TestExecutionListeners(value = DbUnitTestExecutionListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@DatabaseSetup([
        '/com/testwithspring/master/users.xml',
        '/com/testwithspring/master/tasks.xml'
])
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
class ShowTaskSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    def 'Open view task task page as an anonymous user'() {

        def response

        when: 'An anonymous user tries to open the view task page'
        response = openViewTaskPage(Tasks.WriteExampleApp.ID)

        then: 'Should return the HTTP status code found'
        response.andExpect(MockMvcResultMatchers.status().isFound())

        and: 'Should redirect the user to the login page'
        response.andExpect(MockMvcResultMatchers.redirectedUrl(WebTestConstants.LOGIN_PAGE_URL))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    def 'Open view task page as a registered user'() {

        def response

        when: 'The requested task is not found'
        response = openViewTaskPage(Tasks.TASK_ID_NOT_FOUND)

        then: 'Should return the HTTP status code not found'
        response.andExpect(MockMvcResultMatchers.status().isNotFound())

        and: 'Should render the 404 view'
        response.andExpect(MockMvcResultMatchers.view().name(WebTestConstants.ErrorView.NOT_FOUND))

        when: 'The requested task is found'
        response = openViewTaskPage(Tasks.WriteExampleApp.ID)

        then: 'Should return the HTTP status code OK'
        response.andExpect(MockMvcResultMatchers.status().isOk())

        and: 'Should render the view task view'
        response.andExpect(MockMvcResultMatchers.view().name(WebTestConstants.View.VIEW_TASK))

        and: 'Should display the information of the found task'
        response.andExpect(MockMvcResultMatchers.model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ASSIGNEE, allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME,
                                is(Tasks.WriteExampleApp.Assignee.NAME)
                        ),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID,
                                is(Tasks.WriteExampleApp.Assignee.ID)
                        )
                )),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.CLOSER, allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME,
                                is(Tasks.WriteExampleApp.Closer.NAME)
                        ),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID,
                                is(Tasks.WriteExampleApp.Closer.ID)
                        )
                )),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.CREATION_TIME, is(Tasks.WriteExampleApp.CREATION_TIME)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.CREATOR, allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME,
                                is(Tasks.WriteExampleApp.Creator.NAME)
                        ),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID,
                                is(Tasks.WriteExampleApp.Creator.ID)
                        )
                )),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(Tasks.WriteExampleApp.ID)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.MODIFICATION_TIME, is(Tasks.WriteExampleApp.MODIFICATION_TIME)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.MODIFIER, allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME,
                                is(Tasks.WriteExampleApp.Modifier.NAME)
                        ),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID,
                                is(Tasks.WriteExampleApp.Modifier.ID)
                        )
                )),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(Tasks.WriteExampleApp.TITLE)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is(Tasks.WriteExampleApp.DESCRIPTION)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.STATUS, is(Tasks.WriteExampleApp.STATUS)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.RESOLUTION, is(Tasks.WriteExampleApp.RESOLUTION))
        )))

        and: 'Should display one tag of the found task'
        response .andExpect(MockMvcResultMatchers.model().attribute(WebTestConstants.ModelAttributeName.TASK,
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TAGS, hasSize(1))
        ))

        and: 'Should display the information of the found tag'
        response.andExpect(MockMvcResultMatchers.model().attribute(WebTestConstants.ModelAttributeName.TASK,
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
        ))
    }

    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    def 'Open view task page as an administrator'() {

        def response

        when: 'The requested task is not found'
        response = openViewTaskPage(Tasks.TASK_ID_NOT_FOUND)

        then: 'Should return the HTTP status code not found'
        response.andExpect(MockMvcResultMatchers.status().isNotFound())

        and: 'Should render the 404 view'
        response.andExpect(MockMvcResultMatchers.view().name(WebTestConstants.ErrorView.NOT_FOUND))

        when: 'The requested task is found'
        response = openViewTaskPage(Tasks.WriteExampleApp.ID)

        then: 'Should return the HTTP status code OK'
        response.andExpect(MockMvcResultMatchers.status().isOk())

        and: 'Should render the view task view'
        response.andExpect(MockMvcResultMatchers.view().name(WebTestConstants.View.VIEW_TASK))

        and: 'Should display the information of the found task'
        response.andExpect(MockMvcResultMatchers.model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ASSIGNEE, allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME,
                                is(Tasks.WriteExampleApp.Assignee.NAME)
                        ),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID,
                                is(Tasks.WriteExampleApp.Assignee.ID)
                        )
                )),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.CLOSER, allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME,
                                is(Tasks.WriteExampleApp.Closer.NAME)
                        ),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID,
                                is(Tasks.WriteExampleApp.Closer.ID)
                        )
                )),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.CREATION_TIME, is(Tasks.WriteExampleApp.CREATION_TIME)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.CREATOR, allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME,
                                is(Tasks.WriteExampleApp.Creator.NAME)
                        ),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID,
                                is(Tasks.WriteExampleApp.Creator.ID)
                        )
                )),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(Tasks.WriteExampleApp.ID)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.MODIFICATION_TIME, is(Tasks.WriteExampleApp.MODIFICATION_TIME)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.MODIFIER, allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME,
                                is(Tasks.WriteExampleApp.Modifier.NAME)
                        ),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID,
                                is(Tasks.WriteExampleApp.Modifier.ID)
                        )
                )),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(Tasks.WriteExampleApp.TITLE)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is(Tasks.WriteExampleApp.DESCRIPTION)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.STATUS, is(Tasks.WriteExampleApp.STATUS)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.RESOLUTION, is(Tasks.WriteExampleApp.RESOLUTION))
        )))

        and: 'Should display one tag of the found task'
        response .andExpect(MockMvcResultMatchers.model().attribute(WebTestConstants.ModelAttributeName.TASK,
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TAGS, hasSize(1))
        ))

        and: 'Should display the information of the found tag'
        response.andExpect(MockMvcResultMatchers.model().attribute(WebTestConstants.ModelAttributeName.TASK,
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
        ))
    }

    private ResultActions openViewTaskPage(taskId) throws Exception {
        return  mockMvc.perform(MockMvcRequestBuilders.get('/task/{taskId}', taskId))
    }
}
