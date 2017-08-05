package com.testwithspring.master.task

import com.testwithspring.master.UnitTest
import org.junit.experimental.categories.Category
import spock.lang.Specification

/**
 * This specification demonstrates how we can create a new object
 * and set its property values.
 */
@Category(UnitTest.class)
class PropertySpec extends Specification {

    def 'Create a new object and set its property values'() {

        when: 'We create a new object and set its property values'
        def object = new TaskFormDTO()
        object.id = 1L
        object.title = 'title'
        object.description = 'description'

        then: 'Should create a new object and set its property values'
        object.id == 1L
        object.title == 'title'
        object.description == 'description'
    }
}
