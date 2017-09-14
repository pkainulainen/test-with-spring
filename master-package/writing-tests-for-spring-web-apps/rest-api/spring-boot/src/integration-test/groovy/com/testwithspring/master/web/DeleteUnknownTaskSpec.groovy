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
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf

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
class DeleteUnknownTaskSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @ExpectedDatabase(value = '/com/testwithspring/master/tasks.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Delete task as anonymous user'() {

        def response

        when: 'An anonymous user tries to delete an unknown task'
        response = deleteTask(Tasks.TASK_ID_NOT_FOUND)

        then: 'Should return the HTTP status unauthorized'
        response.andExpect(MockMvcResultMatchers.status().isUnauthorized())

        and: 'Should return an empty response'
        response.andExpect(MockMvcResultMatchers.content().string(''))

    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = '/com/testwithspring/master/tasks.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Delete task as a registered user'() {

        def response

        when: 'A registered user tries to delete an unknown task'
        response = deleteTask(Tasks.TASK_ID_NOT_FOUND)

        then: 'Should return the HTTP status code not found'
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    @ExpectedDatabase(value = '/com/testwithspring/master/tasks.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Delete task as an administrator'() {

        def response

        when: 'An administrator tries to delete an unknown task'
        response = deleteTask(Tasks.TASK_ID_NOT_FOUND)

        then: 'Should return the HTTP status code not found'
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    private ResultActions deleteTask(id) {
        return  mockMvc.perform(MockMvcRequestBuilders.delete('/api/task/{taskId}', id)
                .with(csrf())
        )
    }
}
