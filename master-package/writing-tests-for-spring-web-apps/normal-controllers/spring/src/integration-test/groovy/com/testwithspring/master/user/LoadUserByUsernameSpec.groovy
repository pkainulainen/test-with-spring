package com.testwithspring.master.user

import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader
import com.testwithspring.master.IntegrationTest
import com.testwithspring.master.IntegrationTestContext
import com.testwithspring.master.Users
import com.testwithspring.master.config.Profiles
import org.junit.experimental.categories.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

import static org.hamcrest.Matchers.hasSize

@ContextConfiguration(classes = [IntegrationTestContext.class])
@WebAppConfiguration
@TestExecutionListeners(value = DbUnitTestExecutionListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@DatabaseSetup([
        '/com/testwithspring/master/users.xml',
        '/com/testwithspring/master/tasks.xml'
])
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
class LoadUserByUsernameSpec extends Specification {

    @Autowired
    UserDetailsService service

    def 'Find person information by id'() {

        def user

        when: 'The requested user is not found from the database'
        service.loadUserByUsername(Users.USERNAME_NOT_FOUND)

        then: 'Should throw exception'
        thrown UsernameNotFoundException

        when: 'The requested user is found from the database'
        user = service.loadUserByUsername(Users.JohnDoe.EMAIL_ADDRESS)

        then: 'Should return a user that has the correct user information'
        user.id == Users.JohnDoe.ID
        user.name == Users.JohnDoe.NAME
        user.password == Users.JohnDoe.PASSWORD
        user.username == Users.JohnDoe.EMAIL_ADDRESS

        and: 'Should return a user who can be authenticated'
        user.isAccountNonExpired()
        user.isAccountNonLocked()
        user.isEnabled()
        user.isCredentialsNonExpired()

        and: 'Should return a user that has one granted authority'
        def grantedAuthorities = user.getAuthorities()
        grantedAuthorities hasSize(1)

        and: 'Should return a user that has the correct granted authority'
        def grantedAuthority = grantedAuthorities[0]
        grantedAuthority.authority == Users.JohnDoe.ROLE.toString()
    }
}
