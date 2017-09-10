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

import static org.hamcrest.Matchers.allOf
import static org.hamcrest.Matchers.hasProperty
import static org.hamcrest.Matchers.is
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
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
class ProcessInvalidUpdateTaskFormSpec extends Specification {

    @Autowired
    WebApplicationContext webAppContext

    def mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build()
    }

    @ExpectedDatabase(value = '/com/testwithspring/master/tasks.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Submit an invalid update task form as anonymous user'() {

        def response

        when: 'An anonymous user submits an empty update task form'
        response = submitEmptyUpdateTaskForm()

        then: 'Should return the HTTP status code found'
        response.andExpect(status().isFound())

        and: 'Should redirect the user to the login page'
        response.andExpect(redirectedUrl(WebTestConstants.LOGIN_PAGE_URL))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = '/com/testwithspring/master/tasks.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Submit an invalid update task form as a registered user'() {

        def response

        when: 'A registered user submits an empty update task form'
        response = submitEmptyUpdateTaskForm()

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the update task view'
        response.andExpect(view().name(WebTestConstants.View.UPDATE_TASK))

        and: 'Should forward the user to the update task page'
        response.andExpect(forwardedUrl('/WEB-INF/jsp/task/update.jsp'))

        and: 'Should display a validation error about an empty title'
        response.andExpect(model().attributeHasFieldErrorCode(WebTestConstants.ModelAttributeName.TASK,
                WebTestConstants.ModelAttributeProperty.Task.TITLE,
                is(WebTestConstants.ValidationErrorCode.EMPTY_FIELD)
        ))

        and: 'Should display the field values of the update task form'
        response .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is('')),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(''))
        )))

        and: 'Should not change the id of the updated task'
        response .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(Tasks.WriteLesson.ID))
        ))
    }

    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    @ExpectedDatabase(value = '/com/testwithspring/master/tasks.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Submit an invalid update task form as an administrator'() {

        def response

        when: 'An administrator submits an empty update task form'
        response = submitEmptyUpdateTaskForm()

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the update task view'
        response.andExpect(view().name(WebTestConstants.View.UPDATE_TASK))

        and: 'Should forward the user to the update task page'
        response.andExpect(forwardedUrl('/WEB-INF/jsp/task/update.jsp'))

        and: 'Should display a validation error about an empty title'
        response.andExpect(model().attributeHasFieldErrorCode(WebTestConstants.ModelAttributeName.TASK,
                WebTestConstants.ModelAttributeProperty.Task.TITLE,
                is(WebTestConstants.ValidationErrorCode.EMPTY_FIELD)
        ))

        and: 'Should display the field values of the update task form'
        response .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is('')),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(''))
        )))

        and: 'Should not change the id of the updated task'
        response .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(Tasks.WriteLesson.ID))
        ))
    }

    private ResultActions submitEmptyUpdateTaskForm() throws Exception {
        return  mockMvc.perform(post('/task/{taskId}/update', Tasks.WriteLesson.ID)
                .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, '')
                .param(WebTestConstants.ModelAttributeProperty.Task.ID, Tasks.WriteLesson.ID.toString())
                .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, '')
                .with(csrf())
        )
    }
}
