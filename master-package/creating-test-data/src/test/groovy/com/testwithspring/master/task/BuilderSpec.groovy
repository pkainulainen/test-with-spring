package com.testwithspring.master.task

import com.testwithspring.master.UnitTest
import org.junit.experimental.categories.Category
import spock.lang.Specification

import java.time.ZonedDateTime

/**
 * This specification demonstrates how we can create a new object
 * by using a test data builder.
 */
@Category(UnitTest.class)
class BuilderSpec extends Specification {

    def 'Create a new object by using a test data builder'() {

        def final NOW = ZonedDateTime.now()

        when: 'An open task is created'
        def openTask = new TaskBuilder()
                .withId(1L)
                .withCreationTime(NOW)
                .withCreator(3L)
                .withDescription('description')
                .withModificationTime(NOW)
                .withModifier(3L)
                .withTitle('title')
                .withStatusOpen()
                .build()

        then: 'Should create an open task with the correct information'
        openTask.id == 1L
        openTask.assignee == null
        openTask.closer == null
        openTask.creationTime == NOW
        openTask.creator.userId == 3L
        openTask.description == 'description'
        openTask.modificationTime == NOW
        openTask.modifier.userId == 3L
        openTask.title == 'title'
        openTask.resolution == null
        openTask.status == TaskStatus.OPEN
    }
}
