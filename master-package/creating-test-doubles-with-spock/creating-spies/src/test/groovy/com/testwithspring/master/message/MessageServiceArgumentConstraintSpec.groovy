package com.testwithspring.master.message

import com.testwithspring.master.UnitTest
import org.junit.experimental.categories.Category
import spock.lang.Specification

/**
 * This specification class demonstrates how you can use different
 * argument constraints when you stub methods of your spy. Note that real
 * method is not invoked if you have stubbed it.
 */
@Category(UnitTest.class)
class MessageServiceArgumentConstraintSpec extends Specification {

    static ID = 1L

    def repository = Spy(MessageRepositoryImpl)
    def service = new MessageService(repository)

    def 'Get message with id that is equal to expected id'() {

        given: 'A message is found with an id that is equal to 1'
        def found = new Message()
        repository.findById(ID) >> Optional.of(found)

        when: 'We find a message by using the id 1'
        def returned = service.findById(ID)

        then: 'Should return the found message'
        returned == found
    }

    def 'Get message with id that is not equal to configured id'() {

        given: 'A message is found with an id that is not equal to 2'
        def found = new Message()
        repository.findById(!2L) >> Optional.of(found)

        when: 'We find a message by using the id 1'
        def returned = service.findById(ID)

        then: 'Should return the found message'
        returned == found
    }

    def 'Get message with any single argument'() {

        given: 'A message is found with any single argument (null is OK too)'
        def found = new Message()
        repository.findById(_) >> Optional.of(found)

        when: 'We find a message by using null id'
        def returned = service.findById(null)

        then: 'Should return the found message'
        returned == found
    }

    def 'Get message with any argument list'() {

        given: 'A message is found when any argument list is given (an empty argument list is OK too)'
        def found = new Message()
        repository.findById(*_) >> Optional.of(found)

        when: 'We find a message by using the id 1'
        def returned = service.findById(ID)

        then: 'Should return the found message'
        returned == found
    }

    def 'Get message with any non-null argument'() {

        given: 'A message is found with any non-null id'
        def found = new Message()
        repository.findById(!null) >> Optional.of(found)

        when: 'We find a message by using the id 1'
        def returned = service.findById(ID)

        then: 'Should return the found message'
        returned == found
    }

    def 'Get message with any non-null Long argument'() {

        given: 'A message is found with any non-null id that is Long'
        def found = new Message()
        repository.findById(_ as Long) >> Optional.of(found)

        when: 'We find a message by using the id 1'
        def returned = service.findById(ID)

        then: 'Should return the found message'
        returned == found
    }

    def 'Get message with an argument that satisfies a predicate'() {

        given: 'A message is found with an id that satisfies the given predicate'
        def found = new Message()
        repository.findById({it == ID}) >> Optional.of(found)

        when: 'We find a message by using the id 1'
        def returned = service.findById(ID)

        then: 'Should return the found message'
        returned == found
    }
}
