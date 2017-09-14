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

import static org.hamcrest.Matchers.*
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
class UpdateValidTaskSpec extends Specification {

    private static final NEW_DESCRIPTION = 'The old lesson was not good'
    private static final NEW_TITLE = 'Rewrite an existing lesson'

    @Autowired
    MockMvc mockMvc

    @ExpectedDatabase(value = '/com/testwithspring/master/tasks.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Update the information of a task as an anonymous user'() {

        def response

        when: 'An anonymous user tries to update the information of the task'
        response = updateTask()

        then: 'Should return the HTTP status unauthorized'
        response.andExpect(MockMvcResultMatchers.status().isUnauthorized())

        and: 'Should return an empty response'
        response.andExpect(MockMvcResultMatchers.content().string(''))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @ExpectedDatabase(value = 'update-task-as-user-expected.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Update the information of a task as a registered user'() {

        def response

        when: 'A registered user updates the information of the task'
        response = updateTask()

        then: 'Should return the HTTP status code OK'
        response.andExpect(MockMvcResultMatchers.status().isOk())

        and: 'Should return the found task as JSON'
        response.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return the information of the found task'
        response.andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.ASSIGNEE, is(nullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.CLOSER, is(nullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.CREATION_TIME, is(Tasks.WriteLesson.CREATION_TIME_STRING)))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(Tasks.WriteLesson.Creator.ID.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(Tasks.WriteLesson.Creator.NAME)))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(Tasks.WriteLesson.ID.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.MODIFICATION_TIME, is(ConstantDateTimeService.CURRENT_DATE_AND_TIME)))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(Users.JohnDoe.ID.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(Users.JohnDoe.NAME)))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(NEW_TITLE)))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, is(NEW_DESCRIPTION)))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(Tasks.WriteLesson.STATUS.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, is(nullValue())))

        and: 'Should return task that has one tag'
        response .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.TAGS, hasSize(1)))

        and: 'Should return the information of the correct tag'
        response.andExpect(MockMvcResultMatchers.jsonPath('$.tags[0].id', is(Tasks.WriteLesson.Tags.Lesson.ID.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath('$.tags[0].name', is(Tasks.WriteLesson.Tags.Lesson.NAME)))
    }

    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    @ExpectedDatabase(value = 'update-task-as-admin-expected.xml', assertionMode = DatabaseAssertionMode.NON_STRICT)
    def 'Update the information of a task as an administrator'() {

        def response

        when: 'An administrator updates the information of the task'
        response = updateTask()

        then: 'Should return the HTTP status code OK'
        response.andExpect(MockMvcResultMatchers.status().isOk())

        and: 'Should return the found task as JSON'
        response.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return the information of the found task'
        response.andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.ASSIGNEE, is(nullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.CLOSER, is(nullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.CREATION_TIME, is(Tasks.WriteLesson.CREATION_TIME_STRING)))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(Tasks.WriteLesson.Creator.ID.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(Tasks.WriteLesson.Creator.NAME)))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(Tasks.WriteLesson.ID.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.MODIFICATION_TIME, is(ConstantDateTimeService.CURRENT_DATE_AND_TIME)))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(Users.AnneAdmin.ID.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(Users.AnneAdmin.NAME)))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(NEW_TITLE)))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, is(NEW_DESCRIPTION)))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(Tasks.WriteLesson.STATUS.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, is(nullValue())))

        and: 'Should return task that has one tag'
        response .andExpect(MockMvcResultMatchers.jsonPath(WebTestConstants.JsonPathProperty.Task.TAGS, hasSize(1)))

        and: 'Should return the information of the correct tag'
        response.andExpect(MockMvcResultMatchers.jsonPath('$.tags[0].id', is(Tasks.WriteLesson.Tags.Lesson.ID.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath('$.tags[0].name', is(Tasks.WriteLesson.Tags.Lesson.NAME)))
    }

    private ResultActions updateTask() {
        def input = new TaskFormDTO(id: Tasks.WriteLesson.ID, title: NEW_TITLE, description: NEW_DESCRIPTION)
        return mockMvc.perform(MockMvcRequestBuilders.put('/api/task/{id}', Tasks.WriteLesson.ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ObjectIntoJsonConverter.convertObjectIntoJsonBytes(input))
                .with(csrf())
        )
    }
}
