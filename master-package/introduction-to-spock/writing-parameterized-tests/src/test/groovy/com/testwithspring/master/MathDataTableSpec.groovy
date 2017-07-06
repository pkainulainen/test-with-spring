package com.testwithspring.master

import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(UnitTest.class)
class MathDataTableSpec extends Specification {

    def 'Get the bigger number'() {

        expect: 'Should return the bigger number'
        Math.max(a, b) == c

        where:
        a | b || c
        1 | 0 || 1
        2 | 3 || 3
    }
}
