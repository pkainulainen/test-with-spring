package com.testwithspring.master.web

import com.testwithspring.master.TestStringBuilder
import com.testwithspring.master.UnitTest
import com.testwithspring.master.common.NotFoundException
import com.testwithspring.master.task.TagDTO
import com.testwithspring.master.task.TaskCrudService
import com.testwithspring.master.task.TaskDTO
import com.testwithspring.master.task.TaskDTOBuilder
import com.testwithspring.master.task.TaskFormDTO
import com.testwithspring.master.task.TaskListDTO
import com.testwithspring.master.task.TaskResolution
import com.testwithspring.master.task.TaskStatus
import com.testwithspring.master.user.LoggedInUser
import com.testwithspring.master.user.PersonDTO
import org.junit.experimental.categories.Category
import org.springframework.context.support.StaticMessageSource
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.ZonedDateTime

import static com.testwithspring.master.web.WebTestConfig.exceptionResolver
import static com.testwithspring.master.web.WebTestConfig.fixedLocaleResolver
import static com.testwithspring.master.web.WebTestConfig.jspViewResolver
import static org.hamcrest.Matchers.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

@Category(UnitTest.class)
class TaskCrudControllerSpec extends Specification {

    //Validation
    private static final MAX_LENGTH_OF_DESCRIPTION = 500
    private static final MAX_LENGTH_OF_TITLE = 100

    //Task
    private static final ASSIGNEE_ID = 44L
    private static final ASSIGNEE_NAME = 'Anne Assignee'
    private static final CREATION_TIME = ZonedDateTime.now().minusDays(4)
    private static final CREATOR_ID = 99L
    private static final CREATOR_NAME = 'John Doe'
    private static final MODIFICATION_TIME = CREATION_TIME.plusDays(2)
    private static final MODIFIER_ID = 33L
    private static final MODIFIER_NAME = 'Jane Doe'
    private static final TASK_DESCRIPTION = 'description'
    private static final TASK_ID = 1L
    private static final TASK_ID_NOT_FOUND = 99L
    private static final TASK_TITLE = 'title'

    def messageSource = new StaticMessageSource()
    def service = Mock(TaskCrudService)
    def mockMvc = MockMvcBuilders.standaloneSetup(new TaskCrudController(service, messageSource))
            .setHandlerExceptionResolvers(exceptionResolver())
            .setLocaleResolver(fixedLocaleResolver())
            .setViewResolvers(jspViewResolver())
            .build()

    def 'Delete task'() {

        given: 'The message source contains contains the feedback message'
        def final FEEDBACK_MESSAGE_KEY_TASK_DELETED = 'feedback.message.task.deleted'
        def final FEEDBACK_MESSAGE_TASK_DELETED = 'Task deleted'

        messageSource.addMessage(FEEDBACK_MESSAGE_KEY_TASK_DELETED,
                WebTestConfig.LOCALE,
                FEEDBACK_MESSAGE_TASK_DELETED
        )

        def response

        when: 'The deleted task is not found'
        service.delete(TASK_ID_NOT_FOUND) >> { throw new NotFoundException('') }

        and: 'A user tries to delete a task'
        response = mockMvc.perform(get('/task/{taskId}/delete', TASK_ID_NOT_FOUND))

        then: 'Should return the HTTP status code not found'
        response.andExpect(status().isNotFound())

        and: 'Should render the 404 view'
        response.andExpect(view().name(WebTestConstants.ErrorView.NOT_FOUND))

        when: 'The found task is deleted and the deleted task is returned'
        1 * service.delete(TASK_ID) >> new TaskDTO(id: TASK_ID, title: TASK_TITLE)

        and: 'A user deletes the task'
        response = mockMvc.perform(get('/task/{taskId}/delete', TASK_ID))

        then: 'Should return the HTTP status code found'
        response.andExpect(status().isFound())

        and: 'Should redirect the user to the view task list view'
        response.andExpect(view().name(WebTestConstants.RedirectView.SHOW_TASK_LIST))

        and: 'Should show feedback message to the user'
        response.andExpect(flash().attribute(WebTestConstants.FlashMessageKey.FEEDBACK_MESSAGE,
                FEEDBACK_MESSAGE_TASK_DELETED
        ))
    }

    def 'Show create task form'() {

        def response

        when: 'A user opens the create task page'
        response = mockMvc.perform(get('/task/create'))

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render create task view'
        response.andExpect(view().name(WebTestConstants.View.CREATE_TASK))

        and: 'Should render a create task form'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, nullValue()),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, nullValue()),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, nullValue())
        )))
    }

    def 'Create a new task'() {

        given: 'The message source contains the feedback message'
        def final FEEDBACK_MESSAGE_KEY_TASK_CREATED = 'feedback.message.task.created'
        def final FEEDBACK_MESSAGE_TASK_CREATED = 'Task created'

        messageSource.addMessage(FEEDBACK_MESSAGE_KEY_TASK_CREATED,
                WebTestConfig.LOCALE,
                FEEDBACK_MESSAGE_TASK_CREATED
        )

        def final MAX_LENGTH_DESCRIPTION = TestStringBuilder.createStringWithLength(MAX_LENGTH_OF_DESCRIPTION)
        def final MAX_LENGTH_TITLE = TestStringBuilder.createStringWithLength(MAX_LENGTH_OF_TITLE)
        def response

        when: 'A user submits an empty form'
        response = mockMvc.perform(post('/task/create')
                .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, '')
                .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, '')
        )

        then: 'Should return theHTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the create task view'
        response.andExpect(view().name(WebTestConstants.View.CREATE_TASK))

        and: 'Should show a validation error about an empty title'
        response.andExpect(model().attributeHasFieldErrorCode(WebTestConstants.ModelAttributeName.TASK,
                WebTestConstants.ModelAttributeProperty.Task.TITLE,
                is(WebTestConstants.ValidationErrorCode.EMPTY_FIELD)
        ))

        and: 'Should show empty title and description'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is('')),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(''))
        )))

        and: 'Should not modify the id field'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, nullValue())
        ))

        and: 'Should not create a new task'
        0 * service.create(_ as TaskFormDTO, _ as LoggedInUser)

        when: 'A user submits the create task form with too long description'
        def tooLongDescription = TestStringBuilder.createStringWithLength(MAX_LENGTH_OF_DESCRIPTION + 1)

        response = mockMvc.perform(post('/task/create')
                .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, tooLongDescription)
                .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, MAX_LENGTH_TITLE)
        )

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the create task view'
        response.andExpect(view().name(WebTestConstants.View.CREATE_TASK))

        and: 'Should show validation errors about too long description'
        response.andExpect(model().attributeHasFieldErrorCode(WebTestConstants.ModelAttributeName.TASK,
                WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION,
                is(WebTestConstants.ValidationErrorCode.SIZE)
        ))

        and: 'Should show the entered title and description'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is(tooLongDescription)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(MAX_LENGTH_TITLE))
        )))

        and: 'Should not modify the id field'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, nullValue())
        ))

        and: 'Should not create a new task'
        0 * service.create(_ as TaskFormDTO, _ as LoggedInUser)

        when: 'A user submits the create task form with too long title'
        def tooLongTitle = TestStringBuilder.createStringWithLength(MAX_LENGTH_OF_TITLE + 1)

        response = mockMvc.perform(post('/task/create')
                .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, MAX_LENGTH_DESCRIPTION)
                .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, tooLongTitle)
        )

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the create task view'
        response.andExpect(view().name(WebTestConstants.View.CREATE_TASK))

        and: 'Should show validation errors about too long title'
        response.andExpect(model().attributeHasFieldErrorCode(WebTestConstants.ModelAttributeName.TASK,
                WebTestConstants.ModelAttributeProperty.Task.TITLE,
                is(WebTestConstants.ValidationErrorCode.SIZE)
        ))

        and: 'Should show the entered title and description'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is(MAX_LENGTH_DESCRIPTION)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(tooLongTitle))
        )))

        and: 'Should not modify the id field'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, nullValue())
        ))

        and: 'Should not create a new task'
        0 * service.create(_ as TaskFormDTO, _ as LoggedInUser)

        when: 'A user submits the create task form by using valid information'
        response = mockMvc.perform(post('/task/create')
                .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, MAX_LENGTH_DESCRIPTION)
                .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, MAX_LENGTH_TITLE)
        )

        then: 'Should create a new task by using correct information and return the created task'
        1 * service.create({ TaskFormDTO saved ->
            saved.id == null
            saved.description == MAX_LENGTH_DESCRIPTION
            saved.title == MAX_LENGTH_TITLE
        } as TaskFormDTO, _ as LoggedInUser ) >> new TaskDTO(id: TASK_ID, title: MAX_LENGTH_TITLE)

        and: 'Should return the HTTP status code found'
        response.andExpect(status().isFound())

        and: 'Should redirect the user to the view task view'
        response.andExpect(view().name(WebTestConstants.RedirectView.SHOW_TASK))
                .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_ID, is(TASK_ID.toString())))

        and: 'Should show feedback message to the user'
        response.andExpect(flash().attribute(WebTestConstants.FlashMessageKey.FEEDBACK_MESSAGE,
                FEEDBACK_MESSAGE_TASK_CREATED
        ))
    }

    def 'Show task'() {

        def final CLOSER_ID = 931L
        def final CLOSER_NAME = 'Chris Closer'
        def final TAG_ID = 33L
        def final TAG_NAME = 'testing'

        def response

        when: 'No task is found'
        service.findById(TASK_ID_NOT_FOUND) >> { throw new NotFoundException('') }

        and: 'A user tries to open the view task page'
        response = mockMvc.perform(get('/task/{taskId}', TASK_ID_NOT_FOUND))

        then: 'Should return the HTTP status code not found'
        response.andExpect(status().isNotFound())

        and: 'Should render the 404 view'
        response.andExpect(view().name(WebTestConstants.ErrorView.NOT_FOUND))

        when: 'A closed task is found and it has one tag'
        service.findById(TASK_ID) >> {
            new TaskDTOBuilder()
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
        }

        and: 'A user opens the view task page'
        response = mockMvc.perform(get('/task/{taskId}', TASK_ID))

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the view task view'
        response.andExpect(view().name(WebTestConstants.View.VIEW_TASK))

        and: 'Should display the information of the found task'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ASSIGNEE, allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME, is(ASSIGNEE_NAME)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID, is(ASSIGNEE_ID))
                )),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.CLOSER, allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME, is(CLOSER_NAME)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID, is(CLOSER_ID))
                )),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.CREATION_TIME, is(CREATION_TIME)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.CREATOR, allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME, is(CREATOR_NAME)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID, is(CREATOR_ID))
                )),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(TASK_ID)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.MODIFICATION_TIME, is(MODIFICATION_TIME)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.MODIFIER, allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME, is(MODIFIER_NAME)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID, is(MODIFIER_ID))
                )),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(TASK_TITLE)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is(TASK_DESCRIPTION)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.STATUS, is(TaskStatus.CLOSED)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.RESOLUTION, is(TaskResolution.DONE))
        )))

        and: 'Should display one tag of the found task'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TAGS, hasSize(1))
        ))

        and: 'Should display the tag of the found task'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TAGS, contains(
                        allOf(
                                hasProperty(WebTestConstants.ModelAttributeProperty.Tag.ID, is(TAG_ID)),
                                hasProperty(WebTestConstants.ModelAttributeProperty.Tag.NAME, is(TAG_NAME))
                        )
                ))
        ))
    }

    def 'Show task list'() {

        def final FIRST_TASK_ID = 1L
        def final FIRST_TASK_TITLE = 'firstTask'
        def final SECOND_TASK_ID = 33L
        def final SECOND_TASK_TITLE = 'secondTask'

        def response

        when: 'The returned task list is irrelevant'
        1 * service.findAll() >> []

        and: 'A user opens the task list page'
        response = mockMvc.perform(get('/'))

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the task list view'
        response.andExpect(view().name(WebTestConstants.View.TASK_LIST))

        when: 'No tasks is found'
        1 * service.findAll() >> []

        and: 'A user opens the task list page'
        response = mockMvc.perform(get('/'))

        then: 'Should show an empty task list'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, hasSize(0)))

        when: 'Two tasks are found'
        1 * service.findAll() >> [
                new TaskListDTO(id: FIRST_TASK_ID, title: FIRST_TASK_TITLE, status: TaskStatus.OPEN),
                new TaskListDTO(id: SECOND_TASK_ID, title: SECOND_TASK_TITLE, status: TaskStatus.OPEN)
        ]

        and: 'A user opens the task list page'
        response = mockMvc.perform(get('/'))

        then: 'Should show task list that has two tasks'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, hasSize(2)))

        and: 'Should show task list that contains the correct information'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, contains(
                allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(FIRST_TASK_ID)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(FIRST_TASK_TITLE)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.STATUS, is(TaskStatus.OPEN))
                ),
                allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(SECOND_TASK_ID)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(SECOND_TASK_TITLE)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.STATUS, is(TaskStatus.OPEN))
                )
        )))
    }

    def 'Show update task form'() {

        def response

        when: 'The updated task is not found'
        service.findById(TASK_ID_NOT_FOUND) >> { throw new NotFoundException('') }

        and: 'A user tries to open the update task page'
        response = mockMvc.perform(get('/task/{taskId}/update', TASK_ID_NOT_FOUND))

        then: 'Should return the HTTP status code not found'
        response.andExpect(status().isNotFound())

        and: 'Should render the 404 view'
        response.andExpect(view().name(WebTestConstants.ErrorView.NOT_FOUND))

        when: 'The updated task is found'
        service.findById(TASK_ID) >> new TaskDTO(id: TASK_ID, description: TASK_DESCRIPTION, title: TASK_TITLE)

        and: 'A user opens the update task page'
        response = mockMvc.perform(get('/task/{taskId}/update', TASK_ID))

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render update task view'
        response.andExpect(view().name(WebTestConstants.View.UPDATE_TASK))

        and: 'Should render the update task form that contains the information of the updated task'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is(TASK_DESCRIPTION)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(TASK_ID)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(TASK_TITLE))
        )))
    }

    def 'Update the information of an existing task'() {

        given: 'Message source contains the feedback message'

        def final FEEDBACK_MESSAGE_KEY_TASK_UPDATED = 'feedback.message.task.updated'
        def final FEEDBACK_MESSAGE_TASK_UPDATED = 'Task updated'

        messageSource.addMessage(FEEDBACK_MESSAGE_KEY_TASK_UPDATED,
                WebTestConfig.LOCALE,
                FEEDBACK_MESSAGE_TASK_UPDATED
        )

        def final MAX_LENGTH_DESCRIPTION = TestStringBuilder.createStringWithLength(MAX_LENGTH_OF_DESCRIPTION)
        def final MAX_LENGTH_TITLE = TestStringBuilder.createStringWithLength(MAX_LENGTH_OF_TITLE)
        def response

        when: 'A user submits an empty update task form'
        response = mockMvc.perform(post('/task/{taskId}/update', TASK_ID)
                .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, '')
                .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, '')
        )

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the update task view'
        response.andExpect(view().name(WebTestConstants.View.UPDATE_TASK))

        and: 'Should show a validation error about an empty title'
        response.andExpect(model().attributeHasFieldErrorCode(WebTestConstants.ModelAttributeName.TASK,
                WebTestConstants.ModelAttributeProperty.Task.TITLE,
                is(WebTestConstants.ValidationErrorCode.EMPTY_FIELD)
        ))

        and: 'Should show empty title and description'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is('')),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(''))
        )))

        and: 'Should not modify the id field'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(TASK_ID))
        ))

        and: 'Should not update the information of a task'
        0 * service.update(_ as TaskFormDTO, _ as LoggedInUser)

        when: 'A user submits the update task form with too long description'
        def tooLongDescription = TestStringBuilder.createStringWithLength(MAX_LENGTH_OF_DESCRIPTION + 1)

        response = mockMvc.perform(post('/task/{taskId}/update', TASK_ID)
                .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, tooLongDescription)
                .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, MAX_LENGTH_TITLE)
        )

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the update task view'
        response.andExpect(view().name(WebTestConstants.View.UPDATE_TASK))

        and: 'Should show validation errors about too long description'
        response.andExpect(model().attributeHasFieldErrorCode(WebTestConstants.ModelAttributeName.TASK,
                WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION,
                is(WebTestConstants.ValidationErrorCode.SIZE)
        ))

        and: 'Should show the entered title and description'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is(tooLongDescription)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(MAX_LENGTH_TITLE))
        )))

        and: 'Should not modify the id field'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(TASK_ID))
        ))

        and: 'Should not update the information of a task'
        0 * service.update(_ as TaskFormDTO, _ as LoggedInUser)

        when: 'A user submits the update task form with too long title'
        def tooLongTitle = TestStringBuilder.createStringWithLength(MAX_LENGTH_OF_TITLE + 1)

        response = mockMvc.perform(post('/task/{taskId}/update', TASK_ID)
                .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, MAX_LENGTH_DESCRIPTION)
                .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, tooLongTitle)
        )

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the update task view'
        response.andExpect(view().name(WebTestConstants.View.UPDATE_TASK))

        and: 'Should show validation errors about too long title'
        response.andExpect(model().attributeHasFieldErrorCode(WebTestConstants.ModelAttributeName.TASK,
                WebTestConstants.ModelAttributeProperty.Task.TITLE,
                is(WebTestConstants.ValidationErrorCode.SIZE)
        ))

        and: 'Should show the entered title and description'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is(MAX_LENGTH_DESCRIPTION)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(tooLongTitle))
        )))

        and: 'Should not modify the id field'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(TASK_ID))
        ))

        and: 'Should not update the information of a task'
        0 * service.update(_ as TaskFormDTO, _ as LoggedInUser)

        when: 'A user submits the update task form by using the id of an unknown task'
        response = mockMvc.perform(post('/task/{taskId}/update', TASK_ID_NOT_FOUND)
                .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, MAX_LENGTH_DESCRIPTION)
                .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID_NOT_FOUND.toString())
                .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, MAX_LENGTH_TITLE)
        )

        then: 'Should try to update the task by using correct information and throw an exception'
        1 * service.update({ TaskFormDTO updated ->
            updated.id == TASK_ID_NOT_FOUND
            updated.description == MAX_LENGTH_DESCRIPTION
            updated.title == MAX_LENGTH_TITLE
        } as TaskFormDTO, _ as LoggedInUser ) >> { throw new NotFoundException('') }

        and: 'Should return the HTTP status code not found'
        response.andExpect(status().isNotFound())

        and: 'Should render the 404 view'
        response.andExpect(view().name(WebTestConstants.ErrorView.NOT_FOUND))

        when: 'A user submits the update task form by using valid information'
        response = mockMvc.perform(post('/task/{taskId}/update', TASK_ID)
                .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, MAX_LENGTH_DESCRIPTION)
                .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, MAX_LENGTH_TITLE)
        )

        then: 'Should update the information of the task by using correct information and return the updated task'
        1 * service.update({ TaskFormDTO updated ->
            updated.id == TASK_ID
            updated.description == MAX_LENGTH_DESCRIPTION
            updated.title == MAX_LENGTH_TITLE
        } as TaskFormDTO, _ as LoggedInUser ) >> new TaskDTO(id: TASK_ID, title: MAX_LENGTH_TITLE)

        and: 'Should return the HTTP status code found'
        response.andExpect(status().isFound())

        and: 'Should redirect the user to the view task view'
        response.andExpect(view().name(WebTestConstants.RedirectView.SHOW_TASK))
                .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_ID, is(TASK_ID.toString())))

        and: 'Should show feedback message to the user'
        response.andExpect(flash().attribute(WebTestConstants.FlashMessageKey.FEEDBACK_MESSAGE,
                FEEDBACK_MESSAGE_TASK_UPDATED
        ))
    }
}
