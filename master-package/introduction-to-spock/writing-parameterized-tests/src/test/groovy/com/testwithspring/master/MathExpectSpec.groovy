package com.testwithspring.master

import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(UnitTest.class)
class MathExpectSpec extends Specification {

    def 'Get the bigger number'() {

        expect: 'Should return the bigger number'
        Math.max(1, 0) == 1
        Math.max(2, 3) == 3
    }
}
