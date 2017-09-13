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
import com.testwithspring.master.task.TaskFormDTO
import org.junit.experimental.categories.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
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
class UpdateInvalidTaskSpec extends Specification {

    @Autowired
    WebApplicationContext webAppContext

    def mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build()
    }

    @ExpectedDatabase(value = '/com/testwithspring/master/tasks.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Update the information of a task as an anonymous user by using empty title and description'() {

        def response

        when: 'An anonymous user tries to update the information of a task'
        response = updateTask()

        then: 'Should return the HTTP status unauthorized'
        response.andExpect(status().isUnauthorized())

        and: 'Should return an empty response'
        response.andExpect(content().string(''))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = '/com/testwithspring/master/tasks.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Update the information of a task as a registered user by using empty title and description'() {

        def response

        when: 'A registered user tries to update the information of a task'
        response = updateTask()

        then: 'Should return the HTTP status code bad request'
        response.andExpect(status().isBadRequest())

        and: 'Should return validation errors as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return one validation error'
        response.andExpect(jsonPath('$.fieldErrors', hasSize(1)))

        and: 'Should return validation error about the empty title'
        response.andExpect(MockMvcResultMatchers.jsonPath('$.fieldErrors[0].field', is(WebTestConstants.FieldNames.Task.TITLE)))
                .andExpect(MockMvcResultMatchers.jsonPath('$.fieldErrors[0].errorCode',
                is(WebTestConstants.ValidationErrorCode.EMPTY_FIELD)
        ))
    }

    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    @ExpectedDatabase(value = '/com/testwithspring/master/tasks.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Update the information of a task as an administrator by using empty title and description'() {

        def response

        when: 'An administrator tries to update the information of a task'
        response = updateTask()

        then: 'Should return the HTTP status code bad request'
        response.andExpect(status().isBadRequest())

        and: 'Should return validation errors as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return one validation error'
        response.andExpect(jsonPath('$.fieldErrors', hasSize(1)))

        and: 'Should return validation error about the empty title'
        response.andExpect(MockMvcResultMatchers.jsonPath('$.fieldErrors[0].field', is(WebTestConstants.FieldNames.Task.TITLE)))
                .andExpect(MockMvcResultMatchers.jsonPath('$.fieldErrors[0].errorCode',
                is(WebTestConstants.ValidationErrorCode.EMPTY_FIELD)
        ))
    }

    private ResultActions updateTask() {
        def input = new TaskFormDTO(id: Tasks.WriteLesson.ID, title: '', description: '')
        return mockMvc.perform(put('/api/task/{id}', Tasks.WriteLesson.ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ObjectIntoJsonConverter.convertObjectIntoJsonBytes(input))
                .with(csrf())
        )
    }
}
