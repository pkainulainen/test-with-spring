package com.testwithspring.intermediate.user;

import com.testwithspring.intermediate.UnitTest;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.testwithspring.intermediate.TestDoubles.stub;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class LoggedInUserServiceTest {

    private LoggedInUserService service;
    private UserRepository repository;

    @Before
    public void configureSystemUnderTest() {
        repository = stub(UserRepository.class);
        service = new LoggedInUserService(repository);
    }

    public class LoadUserByUsername {

        private final String EMAIL_ADDRESS = "john.doe@gmail.com";

        public class WhenUserIsNotFound {

            @Before
            public void returnEmptyOptional() {
                given(repository.findByEmailAddress(EMAIL_ADDRESS))
                        .willReturn(Optional.empty());
            }

            @Test(expected = UsernameNotFoundException.class)
            public void shouldThrowException() {
                service.loadUserByUsername(EMAIL_ADDRESS);
            }
        }

        public class WhenUserIsFound {

            private final Long ID = 1L;
            private final String NAME = "John Doe";
            private final String PASSWORD = "password";

            @Before
            public void returnFoundUser() {
                User found = new UserBuilder()
                        .isEnabled()
                        .withId(ID)
                        .withEmailAddress(EMAIL_ADDRESS)
                        .withName(NAME)
                        .withPassword(PASSWORD)
                        .withRoleUser()
                        .build();
                given(repository.findByEmailAddress(EMAIL_ADDRESS))
                        .willReturn(Optional.of(found));
            }

            @Test
            public void shouldReturnLoggedInUserWithCorrectId() {
                LoggedInUser user = (LoggedInUser) service.loadUserByUsername(EMAIL_ADDRESS);
                assertThat(user.getId()).isEqualTo(ID);
            }

            @Test
            public void shouldReturnLoggedInUserWithCorrectName() {
                LoggedInUser user = (LoggedInUser) service.loadUserByUsername(EMAIL_ADDRESS);
                assertThat(user.getName()).isEqualTo(NAME);
            }

            @Test
            public void shouldReturnLoggedInUserWithCorrectPassword() {
                LoggedInUser user = (LoggedInUser) service.loadUserByUsername(EMAIL_ADDRESS);
                assertThat(user.getPassword()).isEqualTo(PASSWORD);
            }

            @Test
            public void shouldReturnLoggedInUserWithCorrectUsername() {
                LoggedInUser user = (LoggedInUser) service.loadUserByUsername(EMAIL_ADDRESS);
                assertThat(user.getUsername()).isEqualTo(EMAIL_ADDRESS);
            }

            @Test
            public void shouldReturnLoggedInUserThatHasOneGrantedAuthority() {
                LoggedInUser user = (LoggedInUser) service.loadUserByUsername(EMAIL_ADDRESS);
                assertThat(user.getAuthorities()).hasSize(1);
            }

            @Test
            public void shouldReturnLoggedInUSerThatHasCorrectGrantedAuthority() {
                LoggedInUser user = (LoggedInUser) service.loadUserByUsername(EMAIL_ADDRESS);
                GrantedAuthority authority = user.getAuthorities().iterator().next();

                assertThat(authority.getAuthority()).isEqualTo(UserRole.ROLE_USER.name());
            }

            @Test
            public void shouldReturnLoggedInUserThatIsEnabled() {
                LoggedInUser user = (LoggedInUser) service.loadUserByUsername(EMAIL_ADDRESS);
                assertThat(user.isEnabled()).isTrue();
            }

            @Test
            public void shouldReturnLoggedInUserWhoseAccountIsNotExpired() {
                LoggedInUser user = (LoggedInUser) service.loadUserByUsername(EMAIL_ADDRESS);
                assertThat(user.isAccountNonExpired()).isTrue();
            }

            @Test
            public void shouldReturnLoggedInUserWhoseAccountIsNotLocked() {
                LoggedInUser user = (LoggedInUser) service.loadUserByUsername(EMAIL_ADDRESS);
                assertThat(user.isAccountNonLocked()).isTrue();
            }

            @Test
            public void shouldReturnLoggedInUserWhoseCredentialsAreNotExpired() {
                LoggedInUser user = (LoggedInUser) service.loadUserByUsername(EMAIL_ADDRESS);
                assertThat(user.isCredentialsNonExpired()).isTrue();
            }
        }
    }
}
