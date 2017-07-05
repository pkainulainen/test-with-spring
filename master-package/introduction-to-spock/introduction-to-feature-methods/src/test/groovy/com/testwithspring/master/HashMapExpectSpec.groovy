package com.testwithspring.master

import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(UnitTest.class)
class HashMapExpectSpec extends Specification{

    def map = new HashMap()

    def 'Get value from a map'() {

        given: 'Map contains one key-value pair'
        def key = 'key'
        def value = 1
        map.put(key, value)

        expect: 'Should return the found value'
        map.get(key) == value

        and: 'Should return null when value is not found'
        def incorrectKey = 'incorrectKey'
        map.get(incorrectKey) == null
    }
}
