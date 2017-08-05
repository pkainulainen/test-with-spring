package com.testwithspring.master

import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(UnitTest.class)
class SimpleConditionSpec extends Specification {

    def 'Write an assertion in the then block'() {

        when: 'We create a new list'
        def list = []

        then: 'Should create an empty list'
        list.isEmpty()
        list.size() == 0
    }

    def 'Write an assertion in the expect block'() {

        expect: 'Should return the bigger of two numbers'
        Math.max(1, 2) == 2
    }
}
