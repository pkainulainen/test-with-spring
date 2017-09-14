package com.testwithspring.master

import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(IntegrationTest.class)
class IntegrationSpec extends Specification {

    def 'Example integration test'() {
        expect: 'Should run this test'
        println('Example integration test')
        true
    }
}
