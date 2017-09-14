package com.testwithspring.master.web

import com.testwithspring.master.TestDateTimeBuilder
import com.testwithspring.master.TestStringBuilder
import com.testwithspring.master.UnitTest
import com.testwithspring.master.common.NotFoundException
import com.testwithspring.master.task.TagDTO
import com.testwithspring.master.task.TaskCrudService
import com.testwithspring.master.task.TaskDTOBuilder
import com.testwithspring.master.task.TaskFormDTO
import com.testwithspring.master.task.TaskListDTO
import com.testwithspring.master.task.TaskResolution
import com.testwithspring.master.task.TaskStatus
import com.testwithspring.master.user.LoggedInUser
import com.testwithspring.master.user.PersonDTO
import org.junit.experimental.categories.Category
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static com.testwithspring.master.web.WebTestConfig.fixedLocaleResolver
import static com.testwithspring.master.web.WebTestConfig.objectMapperHttpMessageConverter
import static org.hamcrest.Matchers.contains
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.nullValue
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Category(UnitTest.class)
class TaskCrudControllerSpec extends Specification {

    //JSON Fields
    private static final JSON_FIELD_DESCRIPTION = 'description'
    private static final JSON_FIELD_TITLE = 'title'

    //Validation
    private static final MAX_LENGTH_OF_DESCRIPTION = 500
    private static final MAX_LENGTH_OF_TITLE = 100

    //Task
    private static final ASSIGNEE_ID = 44L
    private static final ASSIGNEE_NAME = 'Anne Assignee'
    private static final CREATION_TIME = TestDateTimeBuilder.parseLocalDateTimeFromUTCDateTime('2017-07-30T11:41:28')
    private static final CREATION_TIME_STRING = TestDateTimeBuilder.transformUTCDateToLocalDateTime('2017-07-30T11:41:28')
    private static final CREATOR_ID = 99L
    private static final CREATOR_NAME = 'John Doe'
    private static final MODIFICATION_TIME = TestDateTimeBuilder.parseLocalDateTimeFromUTCDateTime('2017-07-31T11:41:28')
    private static final MODIFICATION_TIME_STRING = TestDateTimeBuilder.transformUTCDateToLocalDateTime('2017-07-31T11:41:28')
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

    def 'Create a new task'() {

        def final MAX_LENGTH_DESCRIPTION = TestStringBuilder.createStringWithLength(MAX_LENGTH_OF_DESCRIPTION)
        def final MAX_LENGTH_TITLE = TestStringBuilder.createStringWithLength(MAX_LENGTH_OF_TITLE)

        def TOO_LONG_DESCRIPTION = TestStringBuilder.createStringWithLength(MAX_LENGTH_OF_DESCRIPTION + 1)
        def TOO_LONG_TITLE = TestStringBuilder.createStringWithLength(MAX_LENGTH_OF_TITLE + 1)

        def input
        def response

        when: 'A user tries to create a new task by using empty title and description'
        input = new TaskFormDTO(title: '', description: '')
        response =  mockMvc.perform(post('/api/task')
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ObjectIntoJsonConverter.convertObjectIntoJsonBytes(input))
        )

        then: 'Should return the HTTP status code bad request'
        response.andExpect(status().isBadRequest())

        and: 'Should return validation errors as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return one validation error'
        response.andExpect(jsonPath('$.fieldErrors', hasSize(1)))
        
        and: 'Should return validation error about the empty title'
        response.andExpect(jsonPath('$.fieldErrors[0].field', is(JSON_FIELD_TITLE)))
                .andExpect(jsonPath('$.fieldErrors[0].errorCode',
                        is(WebTestConstants.ValidationErrorCode.EMPTY_FIELD)
        ))

        and: 'Should not create a new task'
        0 * service.create(_ as TaskFormDTO, _ as LoggedInUser)

        when: 'A user tries to create a new task by using too long title'
        input = new TaskFormDTO(title: TOO_LONG_TITLE, description: MAX_LENGTH_DESCRIPTION)

        response =  mockMvc.perform(post('/api/task')
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ObjectIntoJsonConverter.convertObjectIntoJsonBytes(input))
        )

        then: 'Should return the HTTP status code bad request'
        response.andExpect(status().isBadRequest())

        and: 'Should return validation errors as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return one validation error'
        response.andExpect(jsonPath('$.fieldErrors', hasSize(1)))

        and: 'Should return validation error about too long title'
        response.andExpect(jsonPath('$.fieldErrors[0].field', is(JSON_FIELD_TITLE)))
                .andExpect(jsonPath('$.fieldErrors[0].errorCode',
                is(WebTestConstants.ValidationErrorCode.SIZE)
        ))

        and: 'Should not create a new task'
        0 * service.create(_ as TaskFormDTO, _ as LoggedInUser)

        when: 'A user tries to create a new task by using too long description'
        input = new TaskFormDTO(title: MAX_LENGTH_TITLE, description: TOO_LONG_DESCRIPTION)
        response =  mockMvc.perform(post('/api/task')
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ObjectIntoJsonConverter.convertObjectIntoJsonBytes(input))
        )

        then: 'Should return the HTTP status code bad request'
        response.andExpect(status().isBadRequest())

        and: 'Should return validation errors as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return one validation error'
        response.andExpect(jsonPath('$.fieldErrors', hasSize(1)))

        and: 'Should return validation error about too long description'
        response.andExpect(jsonPath('$.fieldErrors[0].field', is(JSON_FIELD_DESCRIPTION)))
                .andExpect(jsonPath('$.fieldErrors[0].errorCode',
                is(WebTestConstants.ValidationErrorCode.SIZE)
        ))

        and: 'Should not create a new task'
        0 * service.create(_ as TaskFormDTO, _ as LoggedInUser)

        when: 'A user tries to create a new task by using too title and description'
        input = new TaskFormDTO(title: TOO_LONG_TITLE, description: TOO_LONG_DESCRIPTION)
        response =  mockMvc.perform(post('/api/task')
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ObjectIntoJsonConverter.convertObjectIntoJsonBytes(input))
        )

        then: 'Should return the HTTP status code bad request'
        response.andExpect(status().isBadRequest())

        and: 'Should return validation errors as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return two validation errors'
        response.andExpect(jsonPath('$.fieldErrors', hasSize(2)))

        and: 'Should return validation errors about too long title and description'
        response.andExpect(jsonPath('$.fieldErrors[?(@.field == \'title\')].errorCode',
                contains(WebTestConstants.ValidationErrorCode.SIZE)
        ))
                .andExpect(jsonPath('$.fieldErrors[?(@.field == \'description\')].errorCode',
                contains(WebTestConstants.ValidationErrorCode.SIZE)
        ))

        and: 'Should not create a new task'
        0 * service.create(_ as TaskFormDTO, _ as LoggedInUser)

        when: 'User creates a new task by using valid information'
        input = new TaskFormDTO(title: MAX_LENGTH_TITLE, description: MAX_LENGTH_DESCRIPTION)
        response =  mockMvc.perform(post('/api/task')
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ObjectIntoJsonConverter.convertObjectIntoJsonBytes(input))
        )

        then: 'Should create a new task by using correct information and return the created task'
        1 * service.create({ TaskFormDTO saved ->
            saved.id == null
            saved.description == MAX_LENGTH_DESCRIPTION
            saved.title == MAX_LENGTH_TITLE
        } as TaskFormDTO, _ as LoggedInUser ) >> new TaskDTOBuilder()
                .withId(TASK_ID)
                .withCreationTime(CREATION_TIME)
                .withCreator(new PersonDTO(userId: CREATOR_ID, name: CREATOR_NAME))
                .withDescription(MAX_LENGTH_DESCRIPTION)
                .withModificationTime(CREATION_TIME)
                .withModifier(new PersonDTO(userId: CREATOR_ID, name: CREATOR_NAME))
                .withTitle(MAX_LENGTH_TITLE)
                .withStatusOpen()
                .build()

        and: 'Should return the HTTP status code created'
        response.andExpect(status().isCreated())

        and: 'Should return the created task as json'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return the information of the created task'
        response.andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ASSIGNEE, nullValue()))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CLOSER, nullValue()))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CREATION_TIME, is(CREATION_TIME_STRING)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(CREATOR_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(CREATOR_NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(TASK_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.MODIFICATION_TIME, is(CREATION_TIME_STRING)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(CREATOR_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(CREATOR_NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(MAX_LENGTH_TITLE)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, is(MAX_LENGTH_DESCRIPTION)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(TaskStatus.OPEN.toString())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, nullValue()))

        and: 'Should return a task that has no tags'
        response.andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TAGS, hasSize(0)))
    }

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
                .withCreationTime(CREATION_TIME)
                .withCreator(new PersonDTO(userId: CREATOR_ID, name: CREATOR_NAME))
                .withDescription(TASK_DESCRIPTION)
                .withModificationTime(MODIFICATION_TIME)
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
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CLOSER, nullValue()))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CREATION_TIME, is(CREATION_TIME_STRING)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(CREATOR_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(CREATOR_NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(TASK_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.MODIFICATION_TIME, is(MODIFICATION_TIME_STRING)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(MODIFIER_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(MODIFIER_NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(TASK_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(TASK_TITLE)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, is(TASK_DESCRIPTION)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(TaskStatus.OPEN.toString())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, nullValue()))

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
                .withCreationTime(CREATION_TIME)
                .withCreator(new PersonDTO(userId: CREATOR_ID, name: CREATOR_NAME))
                .withDescription(TASK_DESCRIPTION)
                .withModificationTime(MODIFICATION_TIME)
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
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CREATION_TIME, is(CREATION_TIME_STRING)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(CREATOR_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(CREATOR_NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(TASK_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.MODIFICATION_TIME, is(MODIFICATION_TIME_STRING)))
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

    def 'Update the information of an existing task'() {

        def final MAX_LENGTH_DESCRIPTION = TestStringBuilder.createStringWithLength(MAX_LENGTH_OF_DESCRIPTION)
        def final MAX_LENGTH_TITLE = TestStringBuilder.createStringWithLength(MAX_LENGTH_OF_TITLE)

        def TOO_LONG_DESCRIPTION = TestStringBuilder.createStringWithLength(MAX_LENGTH_OF_DESCRIPTION + 1)
        def TOO_LONG_TITLE = TestStringBuilder.createStringWithLength(MAX_LENGTH_OF_TITLE + 1)

        def input
        def response

        when: 'A user tries to update the information of a task by using empty title and description'
        input = new TaskFormDTO(id: TASK_ID, title: '', description: '')
        response = mockMvc.perform(put('/api/task/{taskId}', TASK_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ObjectIntoJsonConverter.convertObjectIntoJsonBytes(input))
        )

        then: 'Should return the HTTP status code bad request'
        response.andExpect(status().isBadRequest())

        and: 'Should return validation errors as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return one validation error'
        response.andExpect(jsonPath('$.fieldErrors', hasSize(1)))

        and: 'Should return validation error about the empty title'
        response.andExpect(jsonPath('$.fieldErrors[0].field', is(JSON_FIELD_TITLE)))
                .andExpect(jsonPath('$.fieldErrors[0].errorCode',
                is(WebTestConstants.ValidationErrorCode.EMPTY_FIELD)
        ))

        and: 'Should not update the information of a task'
        0 * service.update(_ as TaskFormDTO, _ as LoggedInUser)

        when: 'A user tries to update the information of a task by using too long title'
        input = new TaskFormDTO(id: TASK_ID, title: TOO_LONG_TITLE, description: MAX_LENGTH_DESCRIPTION)

        response = mockMvc.perform(put('/api/task/{taskId}', TASK_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ObjectIntoJsonConverter.convertObjectIntoJsonBytes(input))
        )

        then: 'Should return the HTTP status code bad request'
        response.andExpect(status().isBadRequest())

        and: 'Should return validation errors as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return one validation error'
        response.andExpect(jsonPath('$.fieldErrors', hasSize(1)))

        and: 'Should return validation error about too long title'
        response.andExpect(jsonPath('$.fieldErrors[0].field', is(JSON_FIELD_TITLE)))
                .andExpect(jsonPath('$.fieldErrors[0].errorCode',
                is(WebTestConstants.ValidationErrorCode.SIZE)
        ))

        and: 'Should not update the information of a task'
        0 * service.update(_ as TaskFormDTO, _ as LoggedInUser)

        when: 'A user tries to update the information of a task by using too long description'
        input = new TaskFormDTO(id: TASK_ID, title: MAX_LENGTH_TITLE, description: TOO_LONG_DESCRIPTION)
        response = mockMvc.perform(put('/api/task/{taskId}', TASK_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ObjectIntoJsonConverter.convertObjectIntoJsonBytes(input))
        )

        then: 'Should return the HTTP status code bad request'
        response.andExpect(status().isBadRequest())

        and: 'Should return validation errors as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return one validation error'
        response.andExpect(jsonPath('$.fieldErrors', hasSize(1)))

        and: 'Should return validation error about too long description'
        response.andExpect(jsonPath('$.fieldErrors[0].field', is(JSON_FIELD_DESCRIPTION)))
                .andExpect(jsonPath('$.fieldErrors[0].errorCode',
                is(WebTestConstants.ValidationErrorCode.SIZE)
        ))

        and: 'Should not update the information of a task'
        0 * service.update(_ as TaskFormDTO, _ as LoggedInUser)

        when: 'A user tries to update the information of a task by using too title and description'
        input = new TaskFormDTO(id: TASK_ID, title: TOO_LONG_TITLE, description: TOO_LONG_DESCRIPTION)
        response = mockMvc.perform(put('/api/task/{taskId}', TASK_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ObjectIntoJsonConverter.convertObjectIntoJsonBytes(input))
        )

        then: 'Should return the HTTP status code bad request'
        response.andExpect(status().isBadRequest())

        and: 'Should return validation errors as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return two validation errors'
        response.andExpect(jsonPath('$.fieldErrors', hasSize(2)))

        and: 'Should return validation errors about too long title and description'
        response.andExpect(jsonPath('$.fieldErrors[?(@.field == \'title\')].errorCode',
                contains(WebTestConstants.ValidationErrorCode.SIZE)
        ))
                .andExpect(jsonPath('$.fieldErrors[?(@.field == \'description\')].errorCode',
                contains(WebTestConstants.ValidationErrorCode.SIZE)
        ))

        and: 'Should not update the information of a task'
        0 * service.update(_ as TaskFormDTO, _ as LoggedInUser)

        when: 'A user tries to update the information of a task by using unknown id'
        input = new TaskFormDTO(id: TASK_ID_NOT_FOUND, title: MAX_LENGTH_TITLE, description: MAX_LENGTH_DESCRIPTION)
        response = mockMvc.perform(put('/api/task/{taskId}', TASK_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ObjectIntoJsonConverter.convertObjectIntoJsonBytes(input))
        )

        then: 'Should try to update the task by using correct information and throw an exception'
        1 * service.update({ TaskFormDTO updated ->
            updated.id == TASK_ID_NOT_FOUND
            updated.description == MAX_LENGTH_DESCRIPTION
            updated.title == MAX_LENGTH_TITLE
        } as TaskFormDTO, _ as LoggedInUser ) >> { throw new NotFoundException('') }

        and: 'Should return the HTTP status code not found'
        response.andExpect(status().isNotFound())

        when: 'A user updates the information of an open task by using valid information'
        response = mockMvc.perform(put('/api/task/{taskId}', TASK_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ObjectIntoJsonConverter.convertObjectIntoJsonBytes(input))
        )
        then: 'Should update the information of the task by using correct information and return the updated task'
        1 * service.update({ TaskFormDTO updated ->
            updated.id == TASK_ID
            updated.description == MAX_LENGTH_DESCRIPTION
            updated.title == MAX_LENGTH_TITLE
        } as TaskFormDTO, _ as LoggedInUser ) >> new TaskDTOBuilder()
                .withId(TASK_ID)
                .withCreationTime(CREATION_TIME)
                .withCreator(new PersonDTO(userId: CREATOR_ID, name: CREATOR_NAME))
                .withDescription(MAX_LENGTH_DESCRIPTION)
                .withModificationTime(MODIFICATION_TIME)
                .withModifier(new PersonDTO(userId: MODIFIER_ID, name: MODIFIER_NAME))
                .withTitle(MAX_LENGTH_TITLE)
                .withStatusOpen()
                .build()

        and: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should return the information of the updated task as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return the information of the updated task'
        response.andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ASSIGNEE, nullValue()))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CLOSER, nullValue()))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CREATION_TIME, is(CREATION_TIME_STRING)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(CREATOR_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(CREATOR_NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(TASK_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.MODIFICATION_TIME, is(MODIFICATION_TIME_STRING)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(MODIFIER_ID.intValue())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(MODIFIER_NAME)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(MAX_LENGTH_TITLE)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, is(MAX_LENGTH_DESCRIPTION)))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(TaskStatus.OPEN.toString())))
                .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, nullValue()))

        and: 'Should return a task that has no tags'
        response.andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TAGS, hasSize(0)))
    }
}
