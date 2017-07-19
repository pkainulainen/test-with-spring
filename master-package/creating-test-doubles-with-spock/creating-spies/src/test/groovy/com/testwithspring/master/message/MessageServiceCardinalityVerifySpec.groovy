package com.testwithspring.master.message

import com.testwithspring.master.UnitTest
import org.junit.experimental.categories.Category
import spock.lang.Specification

/**
 * This specification class demonstrates how you can specify
 * the number of expected method invocations when you use spies.
 *
 */
@Category(UnitTest.class)
class MessageServiceCardinalityVerifySpec extends Specification {

    static MESSAGE = 'Hello World!'

    def repository = Spy(MessageRepositoryImpl)
    def service = new MessageService(repository)

    def 'Verify that the findById() method was not invoked'() {
        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        when: 'We create a new message'
        service.create(message)

        then: 'Should not find a message'
        0 * repository.findById(_)
    }

    def 'Verify that the save() method was invoked once'() {
        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        when: 'We create a new message'
        service.create(message)

        then: 'Should save the message'
        1 * repository.save(message)
    }

    def 'Verify that the save() method was invoked between zero and two times'() {
        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        when: 'We create a new message'
        service.create(message)

        then: 'Should save the message'
        (0..2) * repository.save(message)
    }

    def 'Verify that the save() method was invoked at least one time'() {
        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        when: 'We create a new message'
        service.create(message)

        then: 'Should save the message'
        (1.._) * repository.save(message)
    }


    def 'Verify that the save() method was invoked at most one time'() {
        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        when: 'We create a new message'
        service.create(message)

        then: 'Should save the message'
        (_..1) * repository.save(message)
    }

    def 'Verify that the save() method was invoked any number of times'() {
        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        when: 'We create a new message'
        service.create(message)

        then: 'Should save the message'
        _ * repository.save(message)
    }
}
