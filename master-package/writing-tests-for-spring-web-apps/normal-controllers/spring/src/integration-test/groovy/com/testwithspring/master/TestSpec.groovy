package com.testwithspring.master

import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(IntegrationTest.class)
class TestSpec extends Specification {

    def 'Should run integration tests'() {
        expect:
        true
    }
}
