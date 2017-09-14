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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is
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
class UpdateInvalidTaskSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @ExpectedDatabase(value = '/com/testwithspring/master/tasks.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Update the information of a task as an anonymous user by using empty title and description'() {

        def response

        when: 'An anonymous user tries to update the information of a task'
        response = updateTask()

        then: 'Should return the HTTP status unauthorized'
        response.andExpect(MockMvcResultMatchers.status().isUnauthorized())

        and: 'Should return an empty response'
        response.andExpect(MockMvcResultMatchers.content().string(''))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = '/com/testwithspring/master/tasks.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Update the information of a task as a registered user by using empty title and description'() {

        def response

        when: 'A registered user tries to update the information of a task'
        response = updateTask()

        then: 'Should return the HTTP status code bad request'
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: 'Should return validation errors as JSON'
        response.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return one validation error'
        response.andExpect(MockMvcResultMatchers.jsonPath('$.fieldErrors', hasSize(1)))

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
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())

        and: 'Should return validation errors as JSON'
        response.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return one validation error'
        response.andExpect(MockMvcResultMatchers.jsonPath('$.fieldErrors', hasSize(1)))

        and: 'Should return validation error about the empty title'
        response.andExpect(MockMvcResultMatchers.jsonPath('$.fieldErrors[0].field', is(WebTestConstants.FieldNames.Task.TITLE)))
                .andExpect(MockMvcResultMatchers.jsonPath('$.fieldErrors[0].errorCode',
                is(WebTestConstants.ValidationErrorCode.EMPTY_FIELD)
        ))
    }

    private ResultActions updateTask() {
        def input = new TaskFormDTO(id: Tasks.WriteLesson.ID, title: '', description: '')
        return mockMvc.perform(MockMvcRequestBuilders.put('/api/task/{id}', Tasks.WriteLesson.ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ObjectIntoJsonConverter.convertObjectIntoJsonBytes(input))
                .with(csrf())
        )
    }
}
