package com.testwithspring.master.message

import com.testwithspring.master.UnitTest
import org.junit.experimental.categories.Category
import spock.lang.Specification

/**
 * This specification class demonstrates how you can verify
 * interactions that happened between the system under
 * specification and your spy AND configure the response of
 * a stubbed method.
 */
@Category(UnitTest.class)
class MessageServiceVerifyAndStubSpec extends Specification {

    static ID = 1L
    static MESSAGE = 'Hello World!'

    def repository = Spy(MessageRepositoryImpl)
    def service = new MessageService(repository)

    def 'Verify that a message was saved and return the saved message'() {

        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        when: 'We create a new message'
        def returned = service.create(message)

        then: 'Should save the message and return the saved message'
        1 * repository.save(message) >> message

        and: 'Should return the saved message'
        returned == message
    }

    def 'Verify that a message was saved and determine the returned message by using the method parameter'() {

        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        when: 'We create a new message'
        def returned = service.create(message)

        then: 'Should save the message and return the saved message or null'
        1 * repository.save(message) >> { args -> args[0].messageText == MESSAGE ? message : null }

        and: 'Should return the saved message'
        returned == message
    }

    def 'Verify that a message was saved and determine the returned message by using the method parameter (closure declares param)'() {
        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        when: 'We create a new message'
        def returned = service.create(message)

        then: 'Should save the message and return the saved message or null'
        1 * repository.save(message) >> { Message saved -> saved.messageText == MESSAGE ? saved : null }

        and: 'Should return the saved message'
        returned == message
    }

    def 'Verify that a message was saved and configure the returned message by using a closure'() {

        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        when: 'We create a new message'
        def returned = service.create(message)

        then: 'Should save the message and return the saved message'
        1 * repository.save(message) >> { new Message(id: ID, messageText: MESSAGE) }

        and: 'Should return the found message with correct id'
        returned.id == ID

        and: 'Should return the found message with correct message text'
        returned.messageText == MESSAGE
    }

    def 'Verify that a message was saved and return the modified method parameter'() {

        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        when: 'We create a new message'
        def returned = service.create(message)

        then: 'Should save the message and return the saved message'
        1 * repository.save(message) >> { Message saved ->
            saved.id = ID
            return saved
        }

        and: 'Should return the found message with correct id'
        returned.id == ID

        and: 'Should return the found message with correct message text'
        returned.messageText == MESSAGE
    }

    def 'Verify that a message was saved and throw an exception'() {

        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        when: 'We create a new message'
        service.create(message)

        then: 'Should save the message and throw an exception'
        1 * repository.save(message) >> { throw new RuntimeException() }

        and: 'Should throw exception'
        thrown RuntimeException
    }
}
