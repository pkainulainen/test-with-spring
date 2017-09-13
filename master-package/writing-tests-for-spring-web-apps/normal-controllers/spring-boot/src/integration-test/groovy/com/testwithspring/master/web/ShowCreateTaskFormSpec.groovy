package com.testwithspring.master.web

import com.github.springtestdbunit.DbUnitTestExecutionListener
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

import static org.hamcrest.Matchers.*

@SpringBootTest(classes = [IntegrationTestContext.class])
@AutoConfigureMockMvc
@TestExecutionListeners(value = DbUnitTestExecutionListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
class ShowCreateTaskFormSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    def 'Open create task page as an anonymous user'() {

        def response

        when: 'An anonymous user tries to open the create task page'
        response = openCreateTaskPage()

        then: 'Should return the HTTP status code found'
        response.andExpect(MockMvcResultMatchers.status().isFound())

        and: 'Should redirect the user to the login page'
        response.andExpect(MockMvcResultMatchers.redirectedUrl(WebTestConstants.LOGIN_PAGE_URL))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    def 'Open create task page as a registered user'() {

        def response

        when: 'A registered user opens the create task page'
        response = openCreateTaskPage()

        then: 'Should return the HTTP status code OK'
        response.andExpect(MockMvcResultMatchers.status().isOk())

        and: 'Should render the create task view'
        response.andExpect(MockMvcResultMatchers.view().name(WebTestConstants.View.CREATE_TASK))

        and: 'Should display an empty create task form'
        response.andExpect(MockMvcResultMatchers.model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, nullValue()),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, nullValue()),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, nullValue())
        )))
    }

    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    def 'Open create task page as an administrator'() {

        def response

        when: 'An administrator opens the create task page'
        response = openCreateTaskPage()

        then: 'Should return the HTTP status code OK'
        response.andExpect(MockMvcResultMatchers.status().isOk())

        and: 'Should render the create task view'
        response.andExpect(MockMvcResultMatchers.view().name(WebTestConstants.View.CREATE_TASK))

        and: 'Should display an empty create task form'
        response.andExpect(MockMvcResultMatchers.model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, nullValue()),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, nullValue()),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, nullValue())
        )))
    }

    private ResultActions openCreateTaskPage() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get('/task/create'))
    }
}
