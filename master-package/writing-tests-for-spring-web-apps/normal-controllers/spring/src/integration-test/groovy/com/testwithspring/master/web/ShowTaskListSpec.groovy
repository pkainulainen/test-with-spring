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
import static org.hamcrest.Matchers.hasItem
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
class ShowTaskListSpec extends Specification {

    @Autowired
    WebApplicationContext webAppContext

    def mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build()
    }

    def 'Open task list page as an anonymous user'() {

        def response

        when: 'When an anonymous user tries to open the task list page'
        response = openTaskListPage()

        then: 'Should return the HTTP status code found'
        response.andExpect(status().isFound())

        and: 'Should redirect the user to the login page'
        response.andExpect(redirectedUrl(WebTestConstants.LOGIN_PAGE_URL))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    def 'Open task list page as a registered user'() {

        def response

        when: 'A registered user opens the task list page'
        response = openTaskListPage()

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the task list view'
        response.andExpect(view().name(WebTestConstants.View.TASK_LIST))

        and: 'Should forward the user to the task list page'
        response.andExpect(forwardedUrl('/WEB-INF/jsp/task/list.jsp'))

        and: 'Should display two tasks'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, hasSize(2)))

        and: 'Should display the found tasks'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, allOf(
                hasItem(allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(Tasks.WriteExampleApp.ID)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(Tasks.WriteExampleApp.TITLE)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.STATUS, is(Tasks.WriteExampleApp.STATUS))
                )),
                hasItem(allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(Tasks.WriteLesson.ID)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(Tasks.WriteLesson.TITLE)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.STATUS, is(Tasks.WriteLesson.STATUS))
                ))
        )))
    }

    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    def 'Open empty task list page as an administrator'() {

        def response

        when: 'An administrator opens the task list page'
        response = openTaskListPage()

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the task list view'
        response.andExpect(view().name(WebTestConstants.View.TASK_LIST))

        and: 'Should forward the user to the task list page'
        response.andExpect(forwardedUrl('/WEB-INF/jsp/task/list.jsp'))

        and: 'Should display two tasks'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, hasSize(2)))

        and: 'Should display the found tasks'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, allOf(
                hasItem(allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(Tasks.WriteExampleApp.ID)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(Tasks.WriteExampleApp.TITLE)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.STATUS, is(Tasks.WriteExampleApp.STATUS))
                )),
                hasItem(allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(Tasks.WriteLesson.ID)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(Tasks.WriteLesson.TITLE)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.STATUS, is(Tasks.WriteLesson.STATUS))
                ))
        )))
    }

    private ResultActions openTaskListPage() throws Exception {
        return mockMvc.perform(get('/'))
    }
}
