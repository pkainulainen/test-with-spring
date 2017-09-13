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
import static org.hamcrest.Matchers.hasProperty
import static org.hamcrest.Matchers.is
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

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
class ShowUpdateTaskFormSpec extends Specification {

    @Autowired
    WebApplicationContext webAppContext

    def mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build()
    }

    def 'Open update task page as an anonymous user'()  {

        def response

        when: 'An anonymous user tries to open the update task page'
        response = openUpdateTaskPage(Tasks.WriteLesson.ID)

        then: 'Should return the HTTP status code found'
        response.andExpect(status().isFound())

        and: 'Should redirect the user to the login page'
        response.andExpect(redirectedUrl(WebTestConstants.LOGIN_PAGE_URL))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    def 'Open update task page as a registered user'() {

        def response

        when: 'The updated task is not found'
        response = openUpdateTaskPage(Tasks.TASK_ID_NOT_FOUND)

        then: 'Should return the HTTP status code not found'
        response.andExpect(status().isNotFound())

        and: 'Should render the not found view'
        response.andExpect(view().name(WebTestConstants.ErrorView.NOT_FOUND))

        and: 'Should forward the user to the not found page'
        response.andExpect(forwardedUrl('/WEB-INF/jsp/error/404.jsp'))

        when: 'The updated task is found'
        response = openUpdateTaskPage(Tasks.WriteLesson.ID)

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the update task view'
        response.andExpect(view().name(WebTestConstants.View.UPDATE_TASK))

        and: 'Should forward the user to the update task page'
        response.andExpect(forwardedUrl('/WEB-INF/jsp/task/update.jsp'))

        and: 'Should display the update task form with pre-filled data'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is(Tasks.WriteLesson.DESCRIPTION)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(Tasks.WriteLesson.ID)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(Tasks.WriteLesson.TITLE))
        )))
    }

    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    def 'Open update task page as an administrator'() {

        def response

        when: 'The updated task is not found'
        response = openUpdateTaskPage(Tasks.TASK_ID_NOT_FOUND)

        then: 'Should return the HTTP status code not found'
        response.andExpect(status().isNotFound())

        and: 'Should render the not found view'
        response.andExpect(view().name(WebTestConstants.ErrorView.NOT_FOUND))

        and: 'Should forward the user to the not found page'
        response.andExpect(forwardedUrl('/WEB-INF/jsp/error/404.jsp'))

        when: 'The updated task is found'
        response = openUpdateTaskPage(Tasks.WriteLesson.ID)

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the update task view'
        response.andExpect(view().name(WebTestConstants.View.UPDATE_TASK))

        and: 'Should forward the user to the update task page'
        response.andExpect(forwardedUrl('/WEB-INF/jsp/task/update.jsp'))

        and: 'Should display the update task form with pre-filled data'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is(Tasks.WriteLesson.DESCRIPTION)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(Tasks.WriteLesson.ID)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(Tasks.WriteLesson.TITLE))
        )))
    }

    private ResultActions openUpdateTaskPage(taskId) throws Exception {
        return  mockMvc.perform(get('/task/{taskId}/update', taskId))
    }
}
