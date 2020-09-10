package com.testwithspring.master.user

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader
import com.testwithspring.master.DatabaseIntegrationTest
import com.testwithspring.master.Users
import com.testwithspring.master.common.NotFoundException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestExecutionListeners

@DatabaseIntegrationTest
@ExtendWith(SoftAssertionsExtension::class)
@DatabaseSetup(value = [
    "/com/testwithspring/master/users.xml",
    "/com/testwithspring/master/no-tasks-and-tags.xml"
])
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader::class)
@DisplayName("Find the person information of a person by using user id as search criteria")
class FindPersonInformationByUserIdTest(@Autowired private val personFinder: PersonFinder) {

    @Test
    @DisplayName("Should throw exception when no person information is found")
    fun shouldThrowExceptionWhenNoPersonInformationIsFound() {
        assertThatThrownBy { personFinder.findPersonInformationByUserId(Users.ID_NOT_FOUND) }
                .isExactlyInstanceOf(NotFoundException::class.java)
    }

    @Test
    @DisplayName("Should return correct person information when person information is found")
    fun shouldReturnCorrectPersonInformationWhenPersonInformationIsFound(assertions: SoftAssertions) {
        val found = personFinder.findPersonInformationByUserId(Users.JohnDoe.ID)

        assertions.assertThat(found.name)
                .`as`("name")
                .isEqualTo(Users.JohnDoe.NAME)
        assertions.assertThat(found.userId)
                .`as`("userId")
                .isEqualByComparingTo(Users.JohnDoe.ID)
    }
}