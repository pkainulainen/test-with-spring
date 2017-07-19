package com.testwithspring.master.message

import com.testwithspring.master.UnitTest
import org.junit.experimental.categories.Category
import spock.lang.Specification

/**
 * This specification class demonstrates how you can configure the
 * response of a stubbed method when you use spies. Note that real
 * method is not invoked if you have stubbed it.
 */
@Category(UnitTest.class)
class MessageServiceResponseGeneratorSpec extends Specification {

    static ID = 1L
    static MESSAGE = 'Hello World!'
    
    def repository = Spy(MessageRepositoryImpl)
    def service = new MessageService(repository)

    def 'Return the same message on every invocation'() {

        given: 'A message is found with the id 1'
        def found = new Message()
        repository.findById(ID) >> Optional.of(found)

        when: 'We find a message with the id 1'
        def returned = service.findById(ID)

        then: 'Should return the found message'
        returned == found
    }

    def 'Return different message objects on successive invocations'() {

        given: 'Different messages are found on successive invocations'
        def first = new Message()
        def second = new Message()
        repository.findById(ID) >>> [Optional.of(first), Optional.of(second)]

        when: 'We find two objects by using the id 1'
        def firstReturned = service.findById(ID)
        def secondReturned = service.findById(ID)

        then: 'Should return different objects on successive invocations'
        firstReturned == first
        secondReturned == second
    }

    def 'Determine the returned message by using the method parameter'() {

        given: 'A message is found with the id 1'
        def found = new Message()
        repository.findById(_) >> {args -> args[0] == ID ? Optional.of(found) : Optional.empty() }

        when: 'We find a message with the id 1'
        def returned = service.findById(ID)

        then: 'Should return the found message'
        returned == found
    }

    def 'Determine the returned message by using the method parameter (when closure declares the parameter)'() {

        given: 'A message is found with the id 1'
        def found = new Message()
        repository.findById(_) >> {Long id -> id == ID ? Optional.of(found) : Optional.empty() }

        when: 'We find a message with the id 1'
        def returned = service.findById(ID)

        then: 'Should return the found message'
        returned == found
    }

    def 'Configure the returned message by using a closure'() {

        given: 'A message is found with the id 1'
        repository.findById(ID) >> { Optional.of(new Message(id: ID, messageText: MESSAGE)) }

        when: 'We find a message with the id 1'
        def returned = service.findById(ID)

        then: 'Should return the found message with correct id'
        returned.id == ID

        and: 'Should return the found message with correct message text'
        returned.messageText == MESSAGE
    }

    def 'Modify the method parameter and return it'() {

        given: 'We want to create a new message'
        def message = new Message()
        message.messageText = MESSAGE

        and: 'An id is assigned to the saved message before it is returned'
        repository.save(message) >> { Message saved ->
            saved.id = ID
            return saved
        }

        when: 'We create a new message'
        def returned = service.create(message)

        then: 'Should return the saved message with correct id'
        returned.id == ID

        and: 'Should return the saved message with correct message text'
        returned.messageText == MESSAGE
    }

    def 'Throw an exception'() {

        given: 'No message is found with the id 1'
        repository.findById(ID) >> { throw new NotFoundException('not found') }

        when: 'We find a message by using the id 1'
        service.findById(ID)

        then: 'Should throw exception'
        thrown NotFoundException
    }
}
