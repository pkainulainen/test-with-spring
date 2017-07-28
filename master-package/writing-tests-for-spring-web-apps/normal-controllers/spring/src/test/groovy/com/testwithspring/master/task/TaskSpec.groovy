package com.testwithspring.master.task

import com.testwithspring.master.TestStringBuilder
import com.testwithspring.master.UnitTest
import org.junit.experimental.categories.Category
import spock.lang.Specification

import static com.testwithspring.master.task.TaskMatchers.isOpen

@Category(UnitTest.class)
class TaskSpec extends Specification {

    private static final CREATOR_ID = 3L
    private static final DESCRIPTION = 'This example demonstrates how we can use Spock Framework'
    private static final TITLE = 'Write example application'

    def 'Build a new task'() {

        when: 'The creator of the created task is null'
        Task.builder
                .withCreator(null)
                .withDescription(DESCRIPTION)
                .withTitle(TITLE)
                .build()
        then: 'Should throw exception'
        thrown NullPointerException

        when: 'The description of the created task is too long'
        def tooLongDescription = TestStringBuilder.createStringWithLength(501)
        Task.builder
                .withCreator(CREATOR_ID)
                .withDescription(tooLongDescription)
                .withTitle(TITLE)
                .build()
        then: 'Should throw exception'
        thrown IllegalStateException

        when: 'The title of the created task is null'
        Task.builder
                .withCreator(CREATOR_ID)
                .withDescription(DESCRIPTION)
                .withTitle(null)
                .build()
        then: 'Should throw exception'
        thrown NullPointerException

        when: 'The title of the created task is empty'
        Task.builder
                .withCreator(CREATOR_ID)
                .withDescription(DESCRIPTION)
                .withTitle('')
                .build()
        then: 'Should throw exception'
        thrown IllegalStateException

        when: 'The title of the created task is too long'
        def tooLongTitle = TestStringBuilder.createStringWithLength(101)
        Task.builder
                .withCreator(CREATOR_ID)
                .withDescription(DESCRIPTION)
                .withTitle(tooLongTitle)
                .build()
        then: 'Should throw exception'
        thrown IllegalStateException

        when: 'The information of created task is valid'
        def expectedDescription = TestStringBuilder.createStringWithLength(500)
        def expectedTitle = TestStringBuilder.createStringWithLength(100)

        def createdTask = Task.builder
                .withCreator(CREATOR_ID)
                .withDescription(expectedDescription)
                .withTitle(expectedTitle)
                .build()
        then: 'Should create an open task'
        createdTask isOpen()

        and: 'Should not set the assignee of the created task'
        createdTask.assignee == null

        and: 'Should set the creator of the created task'
        createdTask.creator.userId == CREATOR_ID

        and: 'Should not set the creation time of the created task'
        createdTask.creationTime == null

        and: 'Should set the description of the created task'
        createdTask.description == expectedDescription

        and: 'Should not set the id of the created task'
        createdTask.id == null

        and: 'Should not set the modification time of the created task'
        createdTask.modificationTime == null

        and: 'Should set the modifier of the created task'
        createdTask.modifier.userId == CREATOR_ID

        and: 'Should set the title of the created task'
        createdTask.title == expectedTitle

        and: 'Should not set the version of the created task'
        createdTask.version == null

        and: 'Should create a task that has no tags'
        createdTask.tags.isEmpty()
    }
}
