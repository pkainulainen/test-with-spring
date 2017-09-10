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
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

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
class FindPersonInformationByIdSpec extends Specification {

    @Autowired
    UserRepository repository

    def 'Find person information by id'() {

        def result

        when: 'The requested person information is not found from the database'
        result = repository.findPersonInformationById(Users.ID_NOT_FOUND)

        then: 'Should return an empty optional'
        !result.isPresent()

        when: 'The requested person information is found from the database'
        result = repository.findPersonInformationById(Users.AnneAdmin.ID)

        then: 'Should return a nonempty optional'
        result.isPresent()

        and: 'Should return the found person information'
        def person = result.get()

        person.userId == Users.AnneAdmin.ID
        person.name == Users.AnneAdmin.NAME
    }
}
