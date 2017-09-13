package com.testwithspring.master.web

import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
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
class DeleteFoundTaskSpec extends Specification {

    private static final FEEDBACK_MESSAGE_TASK_DELETED = 'Task was deleted successfully.'

    @Autowired
    WebApplicationContext webAppContext

    def mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build()
    }

    @ExpectedDatabase(value = '/com/testwithspring/master/tasks.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Delete task as anonymous user'() {

        def response

        when: 'An anonymous user tries to delete a task'
        response = deleteTask(Tasks.WriteLesson.ID)

        then: 'Should return the HTTP status code found'
        response.andExpect(status().isFound())

        and: 'Should redirect the user to the login page'
        response.andExpect(redirectedUrl(WebTestConstants.LOGIN_PAGE_URL))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = 'delete-task-expected.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Delete task as a registered user'() {

        def response

        when: 'A registered user deletes an existing task'
        response = deleteTask(Tasks.WriteLesson.ID)

        then: 'Should return the HTTP status code Found'
        response.andExpect(status().isFound())

        and: 'Should redirect the user to the view task list view'
        response.andExpect(view().name(WebTestConstants.RedirectView.SHOW_TASK_LIST))

        and: 'Should redirect the user to the view task list page URL'
        response.andExpect(redirectedUrl('/'))

        and: 'Should display the feedback message to the user'
        response.andExpect(flash().attribute(WebTestConstants.FlashMessageKey.FEEDBACK_MESSAGE,
                FEEDBACK_MESSAGE_TASK_DELETED
        ))
    }

    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    @ExpectedDatabase(value = 'delete-task-expected.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Delete task as an administrator'() {

        def response

        when: 'An administrator deletes an existing task'
        response = deleteTask(Tasks.WriteLesson.ID)

        then: 'Should return the HTTP status code Found'
        response.andExpect(status().isFound())

        and: 'Should redirect the user to the view task list view'
        response.andExpect(view().name(WebTestConstants.RedirectView.SHOW_TASK_LIST))

        and: 'Should redirect the user to the view task list page URL'
        response.andExpect(redirectedUrl('/'))

        and: 'Should display the feedback message to the user'
        response.andExpect(flash().attribute(WebTestConstants.FlashMessageKey.FEEDBACK_MESSAGE,
                FEEDBACK_MESSAGE_TASK_DELETED
        ))
    }

    private ResultActions deleteTask(id) {
        return mockMvc.perform(get('/task/{taskId}/delete', id)
                .with(csrf())
        )
    }
}
