package com.testwithspring.master

import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(IntegrationTest.class)
class GetMessageSpec extends Specification {

    def messageService = new MessageService()

    def 'Get message'() {
        expect: 'Should return the correct message'
        println 'Integration test: should return the correct message'
        messageService.getMessage() == 'Hello World!'
    }
}
