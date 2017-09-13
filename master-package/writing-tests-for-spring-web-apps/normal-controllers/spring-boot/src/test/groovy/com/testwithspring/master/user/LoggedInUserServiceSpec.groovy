package com.testwithspring.master.user

import com.testwithspring.master.ReflectionFieldSetter
import com.testwithspring.master.UnitTest
import org.junit.experimental.categories.Category
import org.springframework.security.core.userdetails.UsernameNotFoundException
import spock.lang.Specification

@Category(UnitTest.class)
class LoggedInUserServiceSpec extends Specification {

    private static final EMAIL_ADDRESS = 'john.doe@gmail.com'
    private static final EMAIL_ADDRESS_NOT_FOUND = 'not.found@gmail.com'
    private static final USER_ID = 1L
    private static final NAME = 'John Doe'
    private static final PASSWORD = 'password'

    def repository = Stub(UserRepository)
    def service = new LoggedInUserService(repository)

    def 'Load user by using username as search criteria'() {

        when: 'No user is found'
        repository.findByEmailAddress(EMAIL_ADDRESS_NOT_FOUND) >> Optional.empty()

        and: 'The user is obtained by using username'
        service.loadUserByUsername(EMAIL_ADDRESS_NOT_FOUND)

        then: 'Should throw exception'
        thrown UsernameNotFoundException

        when: 'The requested user is found'
        def user = new User()
        ReflectionFieldSetter.setFieldValue(user, 'id', USER_ID)
        user.@emailAddress = EMAIL_ADDRESS
        user.@enabled = true
        user.@name = NAME
        user.@password = PASSWORD
        user.@role = UserRole.ROLE_USER

        repository.findByEmailAddress(EMAIL_ADDRESS) >> Optional.of(user)

        and: 'The user is obtained by using username'
        def found = service.loadUserByUsername(EMAIL_ADDRESS)

        then: 'Should return a user with the correct user id'
        found.id == USER_ID

        and: 'Should return an enabled user'
        found.enabled

        and: 'Should return a user with the correct name'
        found.name == NAME

        and: 'Should return a user with the correct password'
        found.password == PASSWORD

        and: 'Should return a user with the correct role'
        found.role == UserRole.ROLE_USER

        and: 'Should return a user with correct username'
        found.username == EMAIL_ADDRESS

    }
}
