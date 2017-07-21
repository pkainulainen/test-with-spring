package com.testwithspring.master.message

import com.testwithspring.master.UnitTest
import org.junit.experimental.categories.Category
import spock.lang.Specification

/**
 * This specification class demonstrates how you can verify
 * interactions that happened between the system under
 * specification and your mock.
 */
@Category(UnitTest.class)
class MessageServiceVerifySpec extends Specification {
    
    static MESSAGE = 'Hello World!'

    def repository = Mock(MessageRepository)
    def service = new MessageService(repository)

    def 'Verify that a message that is equal to the expected message was saved'() {

        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        when: 'We create a new message'
        service.create(message)

        then: 'Should save the message'
        1 * repository.save(message)
    }

    def 'Verify that a message that is not equal to the expected message was saved'() {

        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        when: 'We create a new message'
        service.create(message)

        then: 'Should save the message'
        1 * repository.save(!new Message())
    }

    def 'Verify that any message was saved (any single parameter, including null)'() {

        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        when: 'We create a new message'
        service.create(message)

        then: 'Should save any message'
        1 * repository.save(_)
    }

    def 'Verify that any message was saved (any argument list)'() {

        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        when: 'We create a new message'
        service.create(message)

        then: 'Should save any message'
        1 * repository.save(*_)
    }

    def 'Verify that any message was saved (any non-null argument)'() {

        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        when: 'We create a new message'
        service.create(message)

        then: 'Should save any message'
        1 * repository.save(!null)
    }

    def 'Verify that any message was saved (any non-null Message argument)'() {

        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        when: 'We create a new message'
        service.create(message)

        then: 'Should save any message'
        1 * repository.save(_ as Message)
    }

    def 'Verify that a message was saved with the correct message text'() {

        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        when: 'We create a new message'
        service.create(message)

        then: 'Should save a message that has the correct message text'
        1 * repository.save({ it.messageText == MESSAGE })
    }
}
