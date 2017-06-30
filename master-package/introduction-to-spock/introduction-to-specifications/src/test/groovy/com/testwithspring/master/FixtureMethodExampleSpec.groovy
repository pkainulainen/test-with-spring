package com.testwithspring.master

import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(UnitTest.class)
class FixtureMethodExampleSpec extends Specification {

    static MESSAGE = 'Hello World!'
    def messageService = new MessageService()

    def setupSpec() {
        println 'Before the first feature method'
    }

    def setup() {
        println 'Before every feature method'
    }

    def cleanup() {
        println 'After every feature method'
    }

    def cleanupSpec() {
        println 'After the last feature method'
    }

    def 'Get message one'() {
        println 'First feature method'
        expect: 'Should return the correct message'
        messageService.getMessage() == MESSAGE
    }

    def 'Get message two'() {
        println 'Second feature method'
        expect: 'Should return the correct message'
        messageService.getMessage() == MESSAGE
    }
}
