package com.testwithspring.master

import org.junit.experimental.categories.Category
import spock.lang.Shared
import spock.lang.Specification

@Category(UnitTest.class)
class FieldExampleSpec extends Specification {

    static MESSAGE = 'Hello World!'
    def messageService = new MessageService()
    @Shared sharedObject = new Object()

    def 'Get message one'() {
        println 'First feature method'
        println 'unique object: ' + messageService
        println 'shared object: ' + sharedObject

        expect: 'Should return the correct message'
        messageService.getMessage() == MESSAGE
    }

    def 'Get message two'() {
        println 'Second feature method'
        println 'unique object: ' + messageService
        println 'shared object: ' + sharedObject

        expect: 'Should return the correct message'
        messageService.getMessage() == MESSAGE
    }
}
