package com.testwithspring.master

import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(UnitTest.class)
class HashMapWhenThenSpec extends Specification{

    def map = new HashMap()

    def 'Get value from a map'() {

        given: 'Map contains one key-value pair'
        def key = 'key'
        def value = 1
        map.put(key, value)

        when: 'A value is found with the given key'
        def found = map.get(key)

        then: 'Should return the found value'
        found == value

        when: 'A value is not found with the given key'
        def incorrectKey = 'incorrectKey'
        found = map.get(incorrectKey)

        then: 'Should return null'
        found == null
    }
}
