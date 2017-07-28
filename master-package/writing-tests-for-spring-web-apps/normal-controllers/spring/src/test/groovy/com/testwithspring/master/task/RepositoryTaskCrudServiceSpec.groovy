package com.testwithspring.master.task

import com.testwithspring.master.ReflectionFieldSetter
import com.testwithspring.master.UnitTest
import com.testwithspring.master.common.NotFoundException
import com.testwithspring.master.user.LoggedInUser
import com.testwithspring.master.user.PersonDTO
import com.testwithspring.master.user.PersonFinder
import org.junit.experimental.categories.Category
import spock.lang.Specification

import java.time.ZonedDateTime

import static com.testwithspring.master.task.TaskMatchers.isOpen
import static org.hamcrest.Matchers.hasSize

@Category(UnitTest.class)
class RepositoryTaskCrudServiceSpec extends Specification {

    private static final CREATOR_ID = 1L
    private static final CREATOR_NAME = "John Doe"
    private static final DESCRIPTION = "test the method that finds tasks"
    private static final MODIFIER_ID = 456L
    private static final MODIFIER_NAME = "Jane Doe"
    private static final NEW_DESCRIPTION = "Test the method that updates a task";
    private static final NEW_TITLE = "Write new unit test";
    private static final NOW = ZonedDateTime.now()
    private static final TASK_ID = 1L
    private static final TASK_ID_NOT_FOUND = 9L
    private static final TITLE = "Write an example test"
    private static final STATUS = TaskStatus.OPEN

    private static Long TAG_ID = 44L
    private static String TAG_NAME = "testing"

    def personFinder = Stub(PersonFinder)
    def repository = Mock(TaskRepository)
    def service = new RepositoryTaskCrudService(personFinder, repository)

    def 'Create a new task'() {

        given: 'A logged in user wants to create a new task'
        def input = new TaskFormDTO(title: TITLE, description: DESCRIPTION)
        def loggedInUser = new LoggedInUser(id: CREATOR_ID)

        and: 'The creator of the task is found'
        returnCreator()

        when: 'The logged in user creates a new task'
        def created = service.create(input, loggedInUser)

        then: 'Should create a task that has the correct information and return the created task'
        1 * repository.save({ Task saved ->
            saved.assignee == null
            saved.creationTime == null
            saved.creator.userId == CREATOR_ID
            saved.description == DESCRIPTION
            saved.id == null
            saved.modificationTime == null
            saved.modifier.userId == CREATOR_ID
            saved.title == TITLE
            //TODO: Find out if Spock supports Hamcrest matchers in argument constraints
            saved.closer == null
            saved.status == TaskStatus.OPEN
            saved.resolution == null
            saved.tags.isEmpty()
        } as Task) >> { Task saved ->
            ReflectionFieldSetter.setFieldValue(saved, 'id', TASK_ID)
            ReflectionFieldSetter.setFieldValue(saved, 'creationTime', NOW)
            ReflectionFieldSetter.setFieldValue(saved, 'modificationTime', NOW)
            return saved
        }

        and: 'Should return task without assignee'
        created.assignee == null

        and: 'Should return task with the correct creation time'
        created.creationTime == NOW

        and: 'Should return task with the correct creator'
        created.creator.name == CREATOR_NAME
        created.creator.userId == CREATOR_ID

        and: 'Should return task with the correct description'
        created.description == DESCRIPTION

        and: 'Should return task with the correct modification time'
        created.modificationTime == NOW

        and: 'Should return task with the correct modifier'
        created.modifier.name == CREATOR_NAME
        created.modifier.userId == CREATOR_ID

        and: 'Should return task with the correct id'
        created.id == TASK_ID

        and: 'Should return task with the correct title'
        created.title == TITLE

        and: 'Should return an open task'
        created isOpen()

        and: 'Should return task that has no tags'
        created.tags.isEmpty()
    }

    def 'Delete a task'() {

        given: 'A user wants to delete a task that has one tag'
        def tag = new TagBuilder()
                .withId(TAG_ID)
                .withName(TAG_NAME)
                .build()

        def found = new TaskBuilder()
                .withId(TASK_ID)
                .withCreationTime(NOW)
                .withCreator(CREATOR_ID)
                .withDescription(DESCRIPTION)
                .withModificationTime(NOW)
                .withModifier(MODIFIER_ID)
                .withTags(tag)
                .withTitle(TITLE)
                .withStatusOpen()
                .build()

        and: 'The creator of the found task is found'
        returnCreator()

        and: 'The modifier of the found task is found'
        returnModifier()

        when: 'No task is found with the given id'
        repository.findOne(TASK_ID_NOT_FOUND) >> Optional.empty()

        and: 'A User deletes the task'
        service.delete(TASK_ID_NOT_FOUND)

        then: 'Should throw exception'
        thrown NotFoundException

        when: 'When an open task is found with the given id'
        repository.findOne(TASK_ID) >> Optional.of(found)

        and: 'A user deletes the task'
        def deleted = service.delete(TASK_ID)

        then: 'Should delete the task'
        1 * repository.delete(found)

        and: 'Should return task without assignee'
        deleted.assignee == null

        and: 'Should return task with the correct creation time'
        deleted.creationTime == NOW

        and: 'Should return task with the correct creator'
        deleted.creator.name == CREATOR_NAME
        deleted.creator.userId == CREATOR_ID

        and: 'Should return task with the correct description'
        deleted.description == DESCRIPTION

        and: 'Should return task with the correct modification time'
        deleted.modificationTime == NOW

        and: 'Should return task with the correct modifier'
        deleted.modifier.name == MODIFIER_NAME
        deleted.modifier.userId == MODIFIER_ID

        and: 'Should return task with the correct id'
        deleted.id == TASK_ID

        and: 'Should return task with the correct title'
        deleted.title == TITLE

        and: 'Should return an open task'
        deleted isOpen()

        and: 'Should return task that has one tag'
        def returnedTags = deleted.tags
        returnedTags hasSize(1)

        and: 'Should return the information of the correct tag'
        def returnedTag = deleted.tags[0]

        returnedTag.id == TAG_ID
        returnedTag.name == TAG_NAME
    }

    def 'Find All tasks'() {

        def allTasks

        when: 'One tasks is found'
        repository.findAll() >> [new TaskListDTO(id: TASK_ID, title: TITLE, status: STATUS)]

        and: 'A user gets a list of all tasks'
        allTasks = service.findAll()

        then: 'Should return one task'
        allTasks hasSize(1)

        and: 'Should return task with correct information'
        def task = allTasks[0]

        task.id == TASK_ID
        task.title == TITLE
        task.status == STATUS
    }

    def 'Find one task'() {

        when: 'No task is found with the given id'
        repository.findOne(TASK_ID_NOT_FOUND) >> Optional.empty()

        and: 'A user gets one task by using its id'
        service.findById(TASK_ID_NOT_FOUND)

        then: 'Should throw exception'
        thrown NotFoundException
    }

    def 'Update a task'() {

        given: 'A logged in user wants to update the information of a task'
        def input = new TaskFormDTO(title: NEW_TITLE, description: NEW_DESCRIPTION)
        def loggedInUser = new LoggedInUser(id: MODIFIER_ID)

        and: 'The creator of the updated task is found'
        returnCreator()

        and: 'The modifier of the updated task is found'
        returnModifier()

        when: 'The updated task is not found'
        repository.findOne(TASK_ID_NOT_FOUND) >> Optional.empty()

        and: 'The user updates the task'
        input.id = TASK_ID_NOT_FOUND
        service.update(input, loggedInUser)

        then: 'Should throw exception'
        thrown NotFoundException

        when: 'The updated task is found'
        def tag = new TagBuilder()
                .withId(TAG_ID)
                .withName(TAG_NAME)
                .build()

        def found = new TaskBuilder()
                .withId(TASK_ID)
                .withCreationTime(NOW)
                .withCreator(CREATOR_ID)
                .withDescription(DESCRIPTION)
                .withModificationTime(NOW)
                .withModifier(CREATOR_ID)
                .withTags(tag)
                .withTitle(TITLE)
                .withStatusOpen()
                .build()
        repository.findOne(TASK_ID) >> Optional.of(found)

        and: 'The user updates the task'
        input.id = TASK_ID
        def returned = service.update(input, loggedInUser)

        then: 'Should update the title and description of the updated task'
        found.title == NEW_TITLE
        found.description == NEW_DESCRIPTION

        and: 'Should not set the assignee of the updated task'
        found.assignee == null

        and: 'Should not set the closer of the updated task'
        found.closer == null

        and: 'Should not update the creator of the updated task'
        found.creator.userId == CREATOR_ID

        and: 'Should update the modifier of the updated task'
        found.modifier.userId == MODIFIER_ID

        and: 'Should not change the status of the updated task'
        found isOpen()

        and: 'Should not add new tags to the updated task'
        def foundTags = found.tags
        foundTags hasSize(1)

        and: 'Should not update the existing task'
        def foundTag = found.tags[0]
        foundTag.id == TAG_ID
        foundTag.name == TAG_NAME

        and: 'Should return task without assignee'
        returned.assignee == null

        and: 'Should return task with the correct creation time'
        returned.creationTime == NOW

        and: 'Should return task with the correct creator'
        returned.creator.name == CREATOR_NAME
        returned.creator.userId == CREATOR_ID

        and: 'Should return task with the correct description'
        returned.description == NEW_DESCRIPTION

        and: 'Should return task with the correct modification time'
        returned.modificationTime == NOW

        and: 'Should return task with the correct modifier'
        returned.modifier.name == MODIFIER_NAME
        returned.modifier.userId == MODIFIER_ID

        and: 'Should return task with the correct id'
        returned.id == TASK_ID

        and: 'Should return task with the correct title'
        returned.title == NEW_TITLE

        and: 'Should return an open task'
        returned isOpen()

        and: 'Should return task that has one tag'
        def returnedTags = returned.tags
        returnedTags hasSize(1)

        and: 'Should return the information of the correct tag'
        def returnedTag = returned.tags[0]

        returnedTag.id == TAG_ID
        returnedTag.name == TAG_NAME
    }

    private def returnCreator() {
        personFinder.findPersonInformationByUserId(CREATOR_ID) >> new PersonDTO(userId: CREATOR_ID, name: CREATOR_NAME)
    }

    private def returnModifier() {
        personFinder.findPersonInformationByUserId(MODIFIER_ID) >> new PersonDTO(userId: MODIFIER_ID, name: MODIFIER_NAME)
    }
}
