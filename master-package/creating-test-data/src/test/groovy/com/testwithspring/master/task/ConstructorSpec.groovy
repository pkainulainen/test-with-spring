package com.testwithspring.master.task

import com.testwithspring.master.UnitTest
import org.junit.experimental.categories.Category
import spock.lang.Specification

/**
 * This specification class demonstrates how we can create new objects
 * by using the named argument constructor.
 */
@Category(UnitTest.class)
class ConstructorSpec extends Specification {

    def 'Create a new object by using the named argument constructor'() {

        when: 'A new task form object is created by using the named argument constructor'
        def object = new TaskFormDTO(id: 1L, title: 'title', description: 'description')

        then: 'Should create a new object with correct information'
        object.id == 1L
        object.title == 'title'
        object.description == 'description'
    }
}
