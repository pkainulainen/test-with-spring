package com.testwithspring.master.user

import com.testwithspring.master.UnitTest
import com.testwithspring.master.common.NotFoundException
import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(UnitTest.class)
class PersonFinderSpec extends Specification {

    private static final USER_ID = 1L
    private static final USER_ID_NOT_FOUND = 99L
    private static final NAME = 'John Doe'

    def repository = Stub(UserRepository)
    def finder = new PersonFinder(repository)

    def 'Find person information by using user id as search criteria'() {

        when: 'Person information is not found'
        repository.findPersonInformationById(USER_ID_NOT_FOUND) >> Optional.empty()

        and: 'Person information is obtained by using user id'
        finder.findPersonInformationByUserId(USER_ID_NOT_FOUND)

        then: 'Should throw exception'
        thrown NotFoundException

        when: 'The requested person information is found'
        repository.findPersonInformationById(USER_ID) >> Optional.of(new PersonDTO(name: NAME, userId: USER_ID))

        and: 'Person information is obtained by using user id'
        def found = finder.findPersonInformationByUserId(USER_ID)

        then: 'Should return the correct person information'
        found.name == NAME
        found.userId == USER_ID
    }
}
