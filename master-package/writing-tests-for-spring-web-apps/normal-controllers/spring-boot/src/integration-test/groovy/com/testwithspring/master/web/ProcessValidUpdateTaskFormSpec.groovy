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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import static org.hamcrest.Matchers.is

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
class ProcessValidUpdateTaskFormSpec extends Specification {

    private static final FEEDBACK_MESSAGE_TASK_UPDATED = 'The information of the task was updated successfully.'
    private static final NEW_DESCRIPTION = 'The old lesson was not good'
    private static final NEW_TITLE = 'Rewrite an existing lesson'

    @Autowired
    MockMvc mockMvc

    @ExpectedDatabase(value = '/com/testwithspring/master/tasks.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Submit a valid update task form as an anonymous user'() {

        def response

        when: 'An anonymous user submits a valid update task form'
        response = submitUpdateTaskForm()

        then: 'Should return the HTTP status code found'
        response.andExpect(MockMvcResultMatchers.status().isFound())

        and: 'Should redirect the user to the login page'
        response.andExpect(MockMvcResultMatchers.redirectedUrl(WebTestConstants.LOGIN_PAGE_URL))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = 'update-task-as-user-expected.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Submit a valid update task form as a registered user'() {

        def response

        when: 'A registered user submits a valid update task form'
        response = submitUpdateTaskForm()

        then: 'Should return the HTTP status code found'
        response.andExpect(MockMvcResultMatchers.status().isFound())

        and: 'Should redirect the user to the view task view'
        response.andExpect(MockMvcResultMatchers.view().name(WebTestConstants.RedirectView.SHOW_TASK))
                .andExpect(MockMvcResultMatchers.model().attribute(WebTestConstants.ModelAttributeName.TASK_ID, is(Tasks.WriteLesson.ID.toString())))

        and: 'Should redirect the user to the view task page'
        response.andExpect(MockMvcResultMatchers.redirectedUrl('/task/2'))

        and: 'Should display the feedback message to the user'
        response.andExpect(MockMvcResultMatchers.flash().attribute(WebTestConstants.FlashMessageKey.FEEDBACK_MESSAGE,
                FEEDBACK_MESSAGE_TASK_UPDATED
        ))
    }

    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    @ExpectedDatabase(value = 'update-task-as-admin-expected.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Submit a valid update task form as an administrator'() {

        def response

        when: 'An administrator submits a valid update task form'
        response = submitUpdateTaskForm()

        then: 'Should return the HTTP status code found'
        response.andExpect(MockMvcResultMatchers.status().isFound())

        and: 'Should redirect the user to the view task view'
        response.andExpect(MockMvcResultMatchers.view().name(WebTestConstants.RedirectView.SHOW_TASK))
                .andExpect(MockMvcResultMatchers.model().attribute(WebTestConstants.ModelAttributeName.TASK_ID, is(Tasks.WriteLesson.ID.toString())))

        and: 'Should redirect the user to the view task page'
        response.andExpect(MockMvcResultMatchers.redirectedUrl('/task/2'))

        and: 'Should display the feedback message to the user'
        response.andExpect(MockMvcResultMatchers.flash().attribute(WebTestConstants.FlashMessageKey.FEEDBACK_MESSAGE,
                FEEDBACK_MESSAGE_TASK_UPDATED
        ))
    }

    private ResultActions submitUpdateTaskForm() throws Exception {
        return  mockMvc.perform(MockMvcRequestBuilders.post('/task/{taskId}/update', Tasks.WriteLesson.ID)
                .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, NEW_DESCRIPTION)
                .param(WebTestConstants.ModelAttributeProperty.Task.ID, Tasks.WriteLesson.ID.toString())
                .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, NEW_TITLE)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
    }
}
