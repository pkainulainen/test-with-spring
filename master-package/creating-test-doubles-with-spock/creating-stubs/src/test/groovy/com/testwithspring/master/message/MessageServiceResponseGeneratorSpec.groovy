package com.testwithspring.master.message

import com.testwithspring.master.UnitTest
import org.junit.experimental.categories.Category
import spock.lang.Specification

/**
 * This specification demonstrates how you can configure the
 * returned response of a stub.
 */
@Category(UnitTest.class)
class MessageServiceResponseGeneratorSpec extends Specification {

    static ID = 1L
    static MESSAGE = 'Hello World!'

    def repository = Stub(MessageRepository)
    def service = new MessageService(repository)

    def 'return the same message'() {

        given: 'A message is found with the id 1'
        def found = new Message()
        repository.findById(ID) >> Optional.of(found)

        when: 'We find a message with the id 1'
        def returned = service.findById(ID)

        then: 'Should return the found message'
        returned == found
    }

    def 'return different message objects for different invocations'() {

        given: 'Different messages are found for different invocations'
        def first = new Message()
        def second = new Message()
        repository.findById(ID) >>> [Optional.of(first), Optional.of(second)]

        when: 'When find two objects by using the id 1'
        def firstReturned = service.findById(ID)
        def secondReturned = service.findById(ID)

        then: 'Should return found messages'
        firstReturned == first
        secondReturned == second
    }

    def 'determine the returned message by using the method parameter'() {
        given: 'A populated message is found with the id 1'
        def found = new Message()
        repository.findById(_) >> {args -> args[0] == 1L ? Optional.of(found) : Optional.empty()}

        when: 'We find a message with the id 1'
        def returned = service.findById(ID)

        then: 'Should return the found message'
        returned == found
    }

    def 'determine the returned message by using the method parameter (when closure declares the parameter)'() {
        given: 'A populated message is found with the id 1'
        def found = new Message()
        repository.findById(_) >> {Long id -> id == 1L ? Optional.of(found) : Optional.empty()}

        when: 'We find a message with the id 1'
        def returned = service.findById(ID)

        then: 'Should return the found message'
        returned == found
    }

    def 'Configure the returned message by using closure'() {
        given: 'a message is found with the id'
        repository.findById(ID) >> { Optional.of(new Message(id: ID, message: MESSAGE)) }

        when: 'We find a message with the id 1'
        def returned = service.findById(ID)

        then: 'Should return the found message with correct id'
        returned.id == ID

        and: 'Should return the found message with correct message text'
        returned.message == MESSAGE
    }

    def 'Modify the method parameter and return it'() {
        given: 'We want to save a new message'
        def message = new Message()
        message.message = MESSAGE

        and: 'An id is assigned to the saved message before it is returned'
        repository.save(message) >> { Message saved ->
            saved.id = ID
            return saved
        }

        when: 'We save the new message'
        def returned = service.save(message)

        then: 'Should return the saved message with correct id'
        returned.id == ID

        and: 'Should return the saved message with correct message text'
        returned.message == MESSAGE
    }

    def 'throw an exception'() {

        given: 'No message is found with the id 1'
        repository.findById(ID) >> {throw new NotFoundException("not found")}

        when: 'We find a message by using the id 1'
        service.findById(ID)

        then: 'Should throw exception'
        thrown NotFoundException
    }
}
