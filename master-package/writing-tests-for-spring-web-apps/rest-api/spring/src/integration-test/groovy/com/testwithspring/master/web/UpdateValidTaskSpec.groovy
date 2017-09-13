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
import com.testwithspring.master.common.ConstantDateTimeService
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.nullValue
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
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
class UpdateValidTaskSpec extends Specification {

    private static final NEW_DESCRIPTION = 'The old lesson was not good'
    private static final NEW_TITLE = 'Rewrite an existing lesson'

    @Autowired
    WebApplicationContext webAppContext

    def mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build()
    }

    @ExpectedDatabase(value = '/com/testwithspring/master/tasks.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Update the information of a task as an anonymous user'() {

        def response

        when: 'An anonymous user tries to update the information of the task'
        response = updateTask()

        then: 'Should return the HTTP status unauthorized'
        response.andExpect(status().isUnauthorized())

        and: 'Should return an empty response'
        response.andExpect(content().string(''))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = 'update-task-as-user-expected.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Update the information of a task as a registered user'() {

        def response

        when: 'A registered user updates the information of the task'
        response = updateTask()

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should return the found task as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return the information of the found task'
        response.andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ASSIGNEE, is(nullValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CLOSER, is(nullValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CREATION_TIME, is(Tasks.WriteLesson.CREATION_TIME_STRING)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(Tasks.WriteLesson.Creator.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(Tasks.WriteLesson.Creator.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(Tasks.WriteLesson.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.MODIFICATION_TIME, is(ConstantDateTimeService.CURRENT_DATE_AND_TIME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(Users.JohnDoe.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(Users.JohnDoe.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(NEW_TITLE)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, is(NEW_DESCRIPTION)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(Tasks.WriteLesson.STATUS.toString())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, is(nullValue())))

        and: 'Should return task that has one tag'
        response .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TAGS, hasSize(1)))

        and: 'Should return the information of the correct tag'
        response.andExpect(jsonPath('$.tags[0].id', is(Tasks.WriteLesson.Tags.Lesson.ID.intValue())))
                .andExpect(jsonPath('$.tags[0].name', is(Tasks.WriteLesson.Tags.Lesson.NAME)))
    }

    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    @ExpectedDatabase(value = 'update-task-as-admin-expected.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Update the information of a task as an administrator'() {

        def response

        when: 'An administrator updates the information of the task'
        response = updateTask()

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should return the found task as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return the information of the found task'
        response.andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ASSIGNEE, is(nullValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CLOSER, is(nullValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CREATION_TIME, is(Tasks.WriteLesson.CREATION_TIME_STRING)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(Tasks.WriteLesson.Creator.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(Tasks.WriteLesson.Creator.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(Tasks.WriteLesson.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.MODIFICATION_TIME, is(ConstantDateTimeService.CURRENT_DATE_AND_TIME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(Users.AnneAdmin.ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(Users.AnneAdmin.NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(NEW_TITLE)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, is(NEW_DESCRIPTION)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(Tasks.WriteLesson.STATUS.toString())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, is(nullValue())))

        and: 'Should return task that has one tag'
        response .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TAGS, hasSize(1)))

        and: 'Should return the information of the correct tag'
        response.andExpect(jsonPath('$.tags[0].id', is(Tasks.WriteLesson.Tags.Lesson.ID.intValue())))
                .andExpect(jsonPath('$.tags[0].name', is(Tasks.WriteLesson.Tags.Lesson.NAME)))
    }

    private ResultActions updateTask() {
        def input = new TaskFormDTO(id: Tasks.WriteLesson.ID, title: NEW_TITLE, description: NEW_DESCRIPTION)
        return mockMvc.perform(put('/api/task/{id}', Tasks.WriteLesson.ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ObjectIntoJsonConverter.convertObjectIntoJsonBytes(input))
                .with(csrf())
        )
    }
}
