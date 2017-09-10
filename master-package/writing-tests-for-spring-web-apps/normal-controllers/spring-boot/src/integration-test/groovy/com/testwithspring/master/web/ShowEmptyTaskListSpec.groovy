package com.testwithspring.master.web

import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader
import com.testwithspring.master.IntegrationTest
import com.testwithspring.master.IntegrationTestContext
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

import static org.hamcrest.Matchers.hasSize

@SpringBootTest(classes = [IntegrationTestContext.class])
@AutoConfigureMockMvc
@TestExecutionListeners(value = DbUnitTestExecutionListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@DatabaseSetup([
        '/com/testwithspring/master/users.xml',
        '/com/testwithspring/master/no-tasks-and-tags.xml'
])
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
class ShowEmptyTaskListSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    def 'Open empty task list page as an anonymous user'() {

        def response

        when: 'When an anonymous user tries to open the task list page'
        response = openTaskListPage()

        then: 'Should return the HTTP status code found'
        response.andExpect(MockMvcResultMatchers.status().isFound())

        and: 'Should redirect the user to the login page'
        response.andExpect(MockMvcResultMatchers.redirectedUrl(WebTestConstants.LOGIN_PAGE_URL))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    def 'Open empty task list page as a registered user'() {

        def response

        when: 'A registered user opens the task list page'
        response = openTaskListPage()

        then: 'Should return the HTTP status code OK'
        response.andExpect(MockMvcResultMatchers.status().isOk())

        and: 'Should render the task list view'
        response.andExpect(MockMvcResultMatchers.view().name(WebTestConstants.View.TASK_LIST))

        and: 'Should not display any tasks'
        response.andExpect(MockMvcResultMatchers.model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, hasSize(0)))
    }

    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    def 'Open empty task list page as an administrator'() {

        def response

        when: 'An administrator opens the task list page'
        response = openTaskListPage()

        then: 'Should return the HTTP status code OK'
        response.andExpect(MockMvcResultMatchers.status().isOk())

        and: 'Should render the task list view'
        response.andExpect(MockMvcResultMatchers.view().name(WebTestConstants.View.TASK_LIST))

        and: 'Should not display any tasks'
        response.andExpect(MockMvcResultMatchers.model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, hasSize(0)))
    }

    private ResultActions openTaskListPage() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get('/'))
    }
}
