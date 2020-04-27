package com.testwithspring.master.user

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader
import com.testwithspring.master.DatabaseIntegrationTest
import com.testwithspring.master.Users
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

@DatabaseIntegrationTest
@ExtendWith(SoftAssertionsExtension::class)
@DatabaseSetup(value = [
    "/com/testwithspring/master/users.xml",
    "/com/testwithspring/master/no-tasks-and-tags.xml"
])
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader::class)
@DisplayName("Load user by using username as search criteria")
class LoadUserByUsernameTest(@Autowired private val userDetailsService: UserDetailsService) {

    @Test
    @DisplayName("Should throw an exception when no user is found")
    fun shouldThrowExceptionWhenNoUserIsFound() {
        assertThatThrownBy { userDetailsService.loadUserByUsername(Users.EMAIL_ADDRESS_NOT_FOUND) }
                .isExactlyInstanceOf(UsernameNotFoundException::class.java)
    }

    @Test
    @DisplayName("Should return the correct user when user is found")
    fun shouldReturnCorrectUserWhenUserIsFound(assertions: SoftAssertions) {
        val found = userDetailsService.loadUserByUsername(Users.JohnDoe.EMAIL_ADDRESS) as LoggedInUser

        assertions.assertThat(found.id)
                .`as`("id")
                .isEqualByComparingTo(Users.JohnDoe.ID)
        assertions.assertThat(found.username)
                .`as`("username")
                .isEqualTo(Users.JohnDoe.EMAIL_ADDRESS)

        assertions.assertThat(found.authorities)
                .`as`("authorities")
                .containsExactly(SimpleGrantedAuthority(Users.JohnDoe.ROLE.name))
        assertions.assertThat(found.role)
                .`as`("role")
                .isEqualTo(Users.JohnDoe.ROLE)

        assertions.assertThat(found.isAccountNonExpired)
                .`as`("isAccountNonExpired")
                .isTrue
        assertions.assertThat(found.isAccountNonLocked)
                .`as`("isAccountNonLocked")
                .isTrue
        assertions.assertThat(found.isCredentialsNonExpired)
                .`as`("isCredentialsNonExpired")
                .isTrue

        assertions.assertThat(found.password)
                .`as`("password")
                .isEqualTo(Users.JohnDoe.PASSWORD)
    }
}