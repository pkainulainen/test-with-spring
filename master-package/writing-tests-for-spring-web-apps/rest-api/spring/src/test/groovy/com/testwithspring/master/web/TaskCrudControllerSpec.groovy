package com.testwithspring.master.web

import com.testwithspring.master.UnitTest
import com.testwithspring.master.common.NotFoundException
import com.testwithspring.master.task.TagDTO
import com.testwithspring.master.task.TaskCrudService
import com.testwithspring.master.task.TaskDTOBuilder
import com.testwithspring.master.task.TaskListDTO
import com.testwithspring.master.task.TaskResolution
import com.testwithspring.master.task.TaskStatus
import com.testwithspring.master.user.PersonDTO
import org.junit.experimental.categories.Category
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.ZonedDateTime

import static com.testwithspring.master.web.WebTestConfig.fixedLocaleResolver
import static com.testwithspring.master.web.WebTestConfig.objectMapperHttpMessageConverter
import static org.hamcrest.Matchers.comparesEqualTo
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.isEmptyOrNullString
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Category(UnitTest.class)
class TaskCrudControllerSpec extends Specification {

    //Task
    private static final ASSIGNEE_ID = 44L
    private static final ASSIGNEE_NAME = 'Anne Assignee'
    private static final CREATOR_ID = 99L
    private static final CREATOR_NAME = 'John Doe'
    private static final MODIFIER_ID = 33L
    private static final MODIFIER_NAME = 'Jane Doe'
    private static final TASK_DESCRIPTION = 'description'
    private static final TASK_ID = 1L
    private static final TASK_ID_NOT_FOUND = 99L
    private static final TASK_TITLE = 'title'

    private static final TAG_ID = 33L
    private static final TAG_NAME = 'testing'

    def service = Mock(TaskCrudService)
    def mockMvc = MockMvcBuilders.standaloneSetup(new TaskCrudController(service))
            .setControllerAdvice(new TaskErrorHandler())
            .setLocaleResolver(fixedLocaleResolver())
            .setMessageConverters(objectMapperHttpMessageConverter())
            .build()

    def 'Delete task'() {

        def response

        when: 'The deleted task is not found'
        service.delete(TASK_ID_NOT_FOUND) >> {throw new NotFoundException('') }

        and: 'a user tries to delete a task'
        response = mockMvc.perform(delete('/api/task/{taskId}', TASK_ID_NOT_FOUND))

        then: 'Should return the HTTP status code not found'
        response.andExpect(status().isNotFound())

        when: 'The deleted task is found and it has one tag'
        def deleted = new TaskDTOBuilder()
                .withId(TASK_ID)
                .withAssignee(new PersonDTO(userId: ASSIGNEE_ID, name: ASSIGNEE_NAME))
                .withCreator(new PersonDTO(userId: CREATOR_ID, name: CREATOR_NAME))
                .withDescription(TASK_DESCRIPTION)
                .withModifier(new PersonDTO(userId: MODIFIER_ID, name: MODIFIER_NAME))
                .withStatusOpen()
                .withTags(new TagDTO(id: TAG_ID, name: TAG_NAME))
                .withTitle(TASK_TITLE)
                .build()

        and: 'The found task is deleted and the deleted task is returned'
        1 * service.delete(TASK_ID) >> deleted

        and: 'A user deletes the task'
        response = mockMvc.perform(delete('/api/task/{taskId}', TASK_ID))

        then: 'Should return HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should the deleted task as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return the information of the deleted task'
        response.andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Assignee.ID, is(ASSIGNEE_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Assignee.NAME, is(ASSIGNEE_NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(CREATOR_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(CREATOR_NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(TASK_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(MODIFIER_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(MODIFIER_NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(TASK_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(TASK_TITLE)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, is(TASK_DESCRIPTION)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(TaskStatus.OPEN.toString())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, isEmptyOrNullString()))

        and: 'Should return a task that has one tag'
        response.andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TAGS, hasSize(1)))

        and: 'Should return the tag of the deleted task'
        response.andExpect(jsonPath('$.tags[0].id', is(TAG_ID.intValue())))
                .andExpect(jsonPath('$.tags[0].name', is(TAG_NAME)))
    }

    def 'Find all tasks'() {

        def final FIRST_TASK_ID = 1L
        def final FIRST_TASK_TITLE = 'firstTask'
        def final SECOND_TASK_ID = 33L
        def final SECOND_TASK_TITLE = 'secondTask'

        def response

        when: 'The returned task list is irrelevant'
        1 * service.findAll() >> []

        and: 'A user gets the task list'
        response = mockMvc.perform(get('/api/task'))

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should return found tasks as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        when: 'No tasks is found'
        1 * service.findAll() >> []

        and: 'A user gets the task list'
        response = mockMvc.perform(get('/api/task'))

        then: 'Should return empty list'
        response.andExpect(jsonPath('$', hasSize(0)))

        when: 'Two tasks are found'
        1 * service.findAll() >> [
                new TaskListDTO(id: FIRST_TASK_ID, title: FIRST_TASK_TITLE, status: TaskStatus.OPEN),
                new TaskListDTO(id: SECOND_TASK_ID, title: SECOND_TASK_TITLE, status: TaskStatus.OPEN)
        ]

        and: 'A user gets the task list'
        response = mockMvc.perform(get('/api/task'))

        then: 'Should return two tasks'
        response.andExpect(jsonPath('$', hasSize(2)))

        and: 'Should return the correct tasks'
        response.andExpect(jsonPath('$[0].id', is(FIRST_TASK_ID.intValue())))
                .andExpect(jsonPath('$[0].title', is(FIRST_TASK_TITLE)))
                .andExpect(jsonPath('$[0].status', is(TaskStatus.OPEN.toString())))
                .andExpect(jsonPath('$[1].id', is(SECOND_TASK_ID.intValue())))
                .andExpect(jsonPath('$[1].title', is(SECOND_TASK_TITLE)))
                .andExpect(jsonPath('$[1].status', is(TaskStatus.OPEN.toString())))
    }

    def 'Find one task'() {

        def final CLOSER_ID = 931L
        def final CLOSER_NAME = 'Chris Closer'

        def response

        when: 'No task is found'
        service.findById(TASK_ID_NOT_FOUND) >> { throw new NotFoundException('') }

        and: 'A user tries to get the task'
        response = mockMvc.perform(get('/api/task/{taskId}', TASK_ID_NOT_FOUND))

        then: 'Should return the HTTP status code not found'
        response.andExpect(status().isNotFound())

        when: 'A closed task is found and it has one tag'
        service.findById(TASK_ID) >> new TaskDTOBuilder()
                .withId(TASK_ID)
                .withAssignee(new PersonDTO(userId: ASSIGNEE_ID, name: ASSIGNEE_NAME))
                .withCreator(new PersonDTO(userId: CREATOR_ID, name: CREATOR_NAME))
                .withDescription(TASK_DESCRIPTION)
                .withModifier(new PersonDTO(userId: MODIFIER_ID, name: MODIFIER_NAME))
                .withResolutionDone(new PersonDTO(userId: CLOSER_ID, name: CLOSER_NAME))
                .withTags(new TagDTO(id: TAG_ID, name: TAG_NAME))
                .withTitle(TASK_TITLE)
                .build()

        and: 'A user gets the task'
        response = mockMvc.perform(get('/api/task/{taskId}', TASK_ID))

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should return the found task as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return the information of the found task'
        response.andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Assignee.ID, is(ASSIGNEE_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Assignee.NAME, is(ASSIGNEE_NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Closer.ID, is(CLOSER_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Closer.NAME, is(CLOSER_NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(CREATOR_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(CREATOR_NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(TASK_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(MODIFIER_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(MODIFIER_NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(TASK_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(TASK_TITLE)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, is(TASK_DESCRIPTION)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(TaskStatus.CLOSED.toString())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, is(TaskResolution.DONE.toString())))

        and: 'Should return a task that has one tag'
        response.andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TAGS, hasSize(1)))

        and: 'Should return the tag of the found task'
        response.andExpect(jsonPath('$.tags[0].id', is(TAG_ID.intValue())))
                .andExpect(jsonPath('$.tags[0].name', is(TAG_NAME)))
    }
}
