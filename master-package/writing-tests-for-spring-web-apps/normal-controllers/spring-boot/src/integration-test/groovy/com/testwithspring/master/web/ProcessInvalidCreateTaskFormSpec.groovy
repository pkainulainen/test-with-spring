package com.testwithspring.master.web

import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.hamcrest.Matchers.*

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
class ProcessInvalidCreateTaskFormSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @ExpectedDatabase(value = '/com/testwithspring/master/no-tasks-and-tags.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Submit an invalid create task form as an anonymous user'() {

        def response

        when: 'An anonymous user submits an empty create task form'
        response = submitEmptyCreateTaskForm()

        then: 'Should return the HTTP status code found'
        response.andExpect(MockMvcResultMatchers.status().isFound())

        and: 'Should redirect the user to the login page'
        response.andExpect(MockMvcResultMatchers.redirectedUrl(WebTestConstants.LOGIN_PAGE_URL))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = '/com/testwithspring/master/no-tasks-and-tags.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Submit an invalid create task form as a registered user'() {

        def response

        when: 'A registered user submits an empty create task form'
        response = submitEmptyCreateTaskForm()

        then: 'Should return the HTTP status code OK'
        response.andExpect(MockMvcResultMatchers.status().isOk())

        and: 'Should render the create task view'
        response.andExpect(MockMvcResultMatchers.view().name(WebTestConstants.View.CREATE_TASK))

        and: 'Should show a validation error about an empty title'
        response.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode(WebTestConstants.ModelAttributeName.TASK,
                WebTestConstants.ModelAttributeProperty.Task.TITLE,
                is(WebTestConstants.ValidationErrorCode.EMPTY_FIELD)
        ))

        and: 'Should show the field values of the empty create task form'
        response.andExpect(MockMvcResultMatchers.model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is('')),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(''))
        )))

        and: 'Should not modify the hidden id parameter'
        response.andExpect(MockMvcResultMatchers.model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, nullValue())
        )))
    }

    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    @ExpectedDatabase(value = '/com/testwithspring/master/no-tasks-and-tags.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Submit an invalid create task form as an administrator'() {

        def response

        when: 'An administrator submits an empty create task form'
        response = submitEmptyCreateTaskForm()

        then: 'Should return the HTTP status code OK'
        response.andExpect(MockMvcResultMatchers.status().isOk())

        and: 'Should render the create task view'
        response.andExpect(MockMvcResultMatchers.view().name(WebTestConstants.View.CREATE_TASK))

        and: 'Should show a validation error about an empty title'
        response.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode(WebTestConstants.ModelAttributeName.TASK,
                WebTestConstants.ModelAttributeProperty.Task.TITLE,
                is(WebTestConstants.ValidationErrorCode.EMPTY_FIELD)
        ))

        and: 'Should show the field values of the empty create task form'
        response.andExpect(MockMvcResultMatchers.model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is('')),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(''))
        )))

        and: 'Should not modify the hidden id parameter'
        response.andExpect(MockMvcResultMatchers.model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, nullValue())
        )))
    }

    private ResultActions submitEmptyCreateTaskForm() throws Exception {
        return  mockMvc.perform(MockMvcRequestBuilders.post('/task/create')
                .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, '')
                .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, '')
                .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
    }
}
