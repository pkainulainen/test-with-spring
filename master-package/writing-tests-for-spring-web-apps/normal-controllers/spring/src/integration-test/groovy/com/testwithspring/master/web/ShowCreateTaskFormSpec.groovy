package com.testwithspring.master.web

import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.testwithspring.master.IntegrationTest
import com.testwithspring.master.IntegrationTestContext
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
import static org.hamcrest.Matchers.nullValue
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
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
class ShowCreateTaskFormSpec extends Specification {

    @Autowired
    WebApplicationContext webAppContext

    def mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build()
    }

    def 'Open create task page as an anonymous user'() {

        def response

        when: 'An anonymous user tries to open the create task page'
        response = openCreateTaskPage()

        then: 'Should return the HTTP status code found'
        response.andExpect(status().isFound())

        and: 'Should redirect the user to the login page'
        response.andExpect(redirectedUrl(WebTestConstants.LOGIN_PAGE_URL))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    def 'Open create task page as a registered user'() {

        def response

        when: 'A registered user opens the create task page'
        response = openCreateTaskPage()

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the create task view'
        response.andExpect(view().name(WebTestConstants.View.CREATE_TASK))

        and: 'Should forward the user to the create task page'
        response.andExpect(forwardedUrl('/WEB-INF/jsp/task/create.jsp'))

        and: 'Should display an empty create task form'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
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
        response.andExpect(status().isOk())

        and: 'Should render the create task view'
        response.andExpect(view().name(WebTestConstants.View.CREATE_TASK))

        and: 'Should forward the user to the create task page'
        response.andExpect(forwardedUrl('/WEB-INF/jsp/task/create.jsp'))

        and: 'Should display an empty create task form'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, nullValue()),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, nullValue()),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, nullValue())
        )))
    }

    private ResultActions openCreateTaskPage() throws Exception {
        return mockMvc.perform(get('/task/create'))
    }
}
