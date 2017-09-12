package com.testwithspring.master.web

import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader
import com.testwithspring.master.IntegrationTest
import com.testwithspring.master.IntegrationTestContext
import com.testwithspring.master.Tasks
import com.testwithspring.master.Users
import com.testwithspring.master.config.Profiles
import org.junit.experimental.categories.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

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
class FindTaskSpec extends Specification {

    @Autowired
    WebApplicationContext webAppContext

    def mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build()
    }

    def 'Find task as an anonymous user'() {

        def response

        when: 'An anonymous user tries to find the task'
        response = findTask(Tasks.WriteExampleApp.ID)

        then: 'Should return the HTTP status unauthorized'
        response.andExpect(status().isUnauthorized())

        and: 'Should return an empty response'
        response.andExpect(content().string(''))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    def 'Find task as a registered user'() {

        def response

        when: 'The requested task is not found'
        response = findTask(Tasks.TASK_ID_NOT_FOUND)

        then: 'Should return the HTTP status code not found'
        response.andExpect(status().isNotFound())

        when: 'The requested task is found'
        response = findTask(Tasks.WriteExampleApp.ID)

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should return the found task as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return the information of the found task'
        response.andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Assignee.ID, is(Tasks.WriteExampleApp.Assignee.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Assignee.NAME, is(Tasks.WriteExampleApp.Assignee.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Closer.ID, is(Tasks.WriteExampleApp.Closer.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Closer.NAME, is(Tasks.WriteExampleApp.Closer.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CREATION_TIME, is(Tasks.WriteExampleApp.CREATION_TIME_STRING)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(Tasks.WriteExampleApp.Creator.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(Tasks.WriteExampleApp.Creator.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(Tasks.WriteExampleApp.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.MODIFICATION_TIME, is(Tasks.WriteExampleApp.MODIFICATION_TIME_STRING)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(Tasks.WriteExampleApp.Modifier.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(Tasks.WriteExampleApp.Modifier.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(Tasks.WriteExampleApp.TITLE)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, is(Tasks.WriteExampleApp.DESCRIPTION)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(Tasks.WriteExampleApp.STATUS.toString())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, is(Tasks.WriteExampleApp.RESOLUTION.toString())))

        and: 'Should return task that has one tag'
        response .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TAGS, hasSize(1)))

        and: 'Should return the information of the correct tag'
        response.andExpect(jsonPath('$.tags[0].id', is(Tasks.WriteExampleApp.Tags.Example.ID.intValue())))
                .andExpect(jsonPath('$.tags[0].name', is(Tasks.WriteExampleApp.Tags.Example.NAME)))
    }

    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    def 'Find task as an administrator'() {

        def response

        when: 'The requested task is not found'
        response = findTask(Tasks.TASK_ID_NOT_FOUND)

        then: 'Should return the HTTP status code not found'
        response.andExpect(status().isNotFound())

        when: 'The requested task is found'
        response = findTask(Tasks.WriteExampleApp.ID)

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should return the found task as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return the information of the found task'
        response.andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Assignee.ID, is(Tasks.WriteExampleApp.Assignee.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Assignee.NAME, is(Tasks.WriteExampleApp.Assignee.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Closer.ID, is(Tasks.WriteExampleApp.Closer.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Closer.NAME, is(Tasks.WriteExampleApp.Closer.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CREATION_TIME, is(Tasks.WriteExampleApp.CREATION_TIME_STRING)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(Tasks.WriteExampleApp.Creator.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(Tasks.WriteExampleApp.Creator.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(Tasks.WriteExampleApp.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.MODIFICATION_TIME, is(Tasks.WriteExampleApp.MODIFICATION_TIME_STRING)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(Tasks.WriteExampleApp.Modifier.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(Tasks.WriteExampleApp.Modifier.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(Tasks.WriteExampleApp.TITLE)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, is(Tasks.WriteExampleApp.DESCRIPTION)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(Tasks.WriteExampleApp.STATUS.toString())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, is(Tasks.WriteExampleApp.RESOLUTION.toString())))

        and: 'Should return task that has one tag'
        response .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TAGS, hasSize(1)))

        and: 'Should return the information of the correct tag'
        response.andExpect(jsonPath('$.tags[0].id', is(Tasks.WriteExampleApp.Tags.Example.ID.intValue())))
                .andExpect(jsonPath('$.tags[0].name', is(Tasks.WriteExampleApp.Tags.Example.NAME)))
    }

    private ResultActions findTask(id) {
        return mockMvc.perform(get('/api/task/{taskId}', id))
    }
}
