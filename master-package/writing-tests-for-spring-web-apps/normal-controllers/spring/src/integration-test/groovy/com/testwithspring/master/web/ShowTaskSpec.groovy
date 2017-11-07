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
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.hamcrest.Matchers.allOf
import static org.hamcrest.Matchers.contains
import static org.hamcrest.Matchers.hasProperty
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ContextConfiguration(classes = [IntegrationTestContext.class])
@WebAppConfiguration
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
    WebApplicationContext webAppContext

    def mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build()
    }

    def 'Open view task task page as an anonymous user'() {

        def response

        when: 'An anonymous user tries to open the view task page'
        response = openViewTaskPage(Tasks.WriteExampleApp.ID)

        then: 'Should return the HTTP status code found'
        response.andExpect(status().isFound())

        and: 'Should redirect the user to the login page'
        response.andExpect(redirectedUrl(WebTestConstants.LOGIN_PAGE_URL))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    def 'Open view task page as a registered user'() {

        def response

        when: 'The requested task is not found'
        response = openViewTaskPage(Tasks.TASK_ID_NOT_FOUND)

        then: 'Should return the HTTP status code not found'
        response.andExpect(status().isNotFound())

        and: 'Should render the 404 view'
        response.andExpect(view().name(WebTestConstants.ErrorView.NOT_FOUND))

        and: 'Should forward the user to the 404 page'
        response.andExpect(forwardedUrl('/WEB-INF/jsp/error/404.jsp'))

        when: 'The requested task is found'
        response = openViewTaskPage(Tasks.WriteExampleApp.ID)

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the view task view'
        response.andExpect(view().name(WebTestConstants.View.VIEW_TASK))

        and: 'Should forward the user to the view task page'
        response.andExpect(forwardedUrl('/WEB-INF/jsp/task/view.jsp'))

        and: 'Should display the information of the found task'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
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
        response .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TAGS, hasSize(1))
        ))

        and: 'Should display the information of the found tag'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
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
        response.andExpect(status().isNotFound())

        and: 'Should render the 404 view'
        response.andExpect(view().name(WebTestConstants.ErrorView.NOT_FOUND))

        and: 'Should forward the user to the 404 page'
        response.andExpect(forwardedUrl('/WEB-INF/jsp/error/404.jsp'))

        when: 'The requested task is found'
        response = openViewTaskPage(Tasks.WriteExampleApp.ID)

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the view task view'
        response.andExpect(view().name(WebTestConstants.View.VIEW_TASK))

        and: 'Should forward the user to the view task page'
        response.andExpect(forwardedUrl('/WEB-INF/jsp/task/view.jsp'))

        and: 'Should display the information of the found task'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
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
        response .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TAGS, hasSize(1))
        ))

        and: 'Should display the information of the found tag'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
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
        return  mockMvc.perform(get('/task/{taskId}', taskId))
    }
}
