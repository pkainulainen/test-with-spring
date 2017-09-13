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
class ProcessValidUpdateTaskFormForUnknownTaskSpec extends Specification {

    private static final NEW_DESCRIPTION = 'The old lesson was not good'
    private static final NEW_TITLE = 'Rewrite an existing lesson'

    @Autowired
    MockMvc mockMvc

    @ExpectedDatabase(value = '/com/testwithspring/master/tasks.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Submit a valid update task form as an anonymous user when the updated task is not found'() {

        def response

        when: 'An anonymous user submits a valid update task form'
        response = submitUpdateTaskForm()

        then: 'Should return the HTTP status code found'
        response.andExpect(MockMvcResultMatchers.status().isFound())

        and: 'Should redirect the user to the login page'
        response.andExpect(MockMvcResultMatchers.redirectedUrl(WebTestConstants.LOGIN_PAGE_URL))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = '/com/testwithspring/master/tasks.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Submit a valid update task form as a registered user when the updated task is not found'() {

        def response

        when: 'A registered user submits a valid update task form'
        response = submitUpdateTaskForm()

        then: 'Should return the HTTP status code not found'
        response.andExpect(MockMvcResultMatchers.status().isNotFound())

        and: 'Should render the not found view'
        response.andExpect(MockMvcResultMatchers.view().name(WebTestConstants.ErrorView.NOT_FOUND))
    }

    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    @ExpectedDatabase(value = '/com/testwithspring/master/tasks.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Submit a valid update task form as an administrator when the updated task is not found'() {

        def response

        when: 'An administrator submits a valid update task form'
        response = submitUpdateTaskForm()

        then: 'Should return the HTTP status code not found'
        response.andExpect(MockMvcResultMatchers.status().isNotFound())

        and: 'Should render the not found view'
        response.andExpect(MockMvcResultMatchers.view().name(WebTestConstants.ErrorView.NOT_FOUND))
    }

    private ResultActions submitUpdateTaskForm() throws Exception {
        return  mockMvc.perform(MockMvcRequestBuilders.post('/task/{taskId}/update', Tasks.TASK_ID_NOT_FOUND)
                .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, NEW_DESCRIPTION)
                .param(WebTestConstants.ModelAttributeProperty.Task.ID, Tasks.TASK_ID_NOT_FOUND.toString())
                .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, NEW_TITLE)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
    }
}
