package com.testwithspring.intermediate.user;

import com.testwithspring.intermediate.UnitTest;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.security.core.GrantedAuthority;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class LoggedInUserTest {

    public class CreateNew {

        private final Long ID = 1L;
        private final String NAME = "John Doe";
        private final String PASSWORD = "password";
        private final String USERNAME = "johndoe";

        private User input;

        @Before
        public void createUser() {
            input = new UserBuilder()
                    .isEnabled()
                    .withId(ID)
                    .withName(NAME)
                    .withPassword(PASSWORD)
                    .withRoleUser()
                    .withUsername(USERNAME)
                    .build();
        }

        @Test
        public void shouldCreateLoggedInUserWithCorrectId() {
            LoggedInUser user = new LoggedInUser(input);
            assertThat(user.getId()).isEqualByComparingTo(ID);
        }

        @Test
        public void shouldCreateLoggedInUserWithCorrectName() {
            LoggedInUser user = new LoggedInUser(input);
            assertThat(user.getName()).isEqualTo(NAME);
        }

        @Test
        public void shouldCreateLoggedInUserWithCorrectPassword() {
            LoggedInUser user = new LoggedInUser(input);
            assertThat(user.getPassword()).isEqualTo(PASSWORD);
        }

        @Test
        public void shouldCreateLoggedInUserWithOneGrantedAuthority() {
            LoggedInUser user = new LoggedInUser(input);
            assertThat(user.getAuthorities()).hasSize(1);
        }

        @Test
        public void shouldCreateLoggedInUserWithCorrectGrantedAuthority() {
            LoggedInUser user = new LoggedInUser(input);
            GrantedAuthority authority = user.getAuthorities().iterator().next();

            assertThat(authority.getAuthority()).isEqualTo(UserRole.ROLE_USER.name());
        }

        @Test
        public void shouldCreateLoggedInUserThatIsEnabled() {
            LoggedInUser user = new LoggedInUser(input);
            assertThat(user.isEnabled()).isTrue();
        }

        @Test
        public void shouldCreateLoggedInUserWhoseAccountIsNotExpired() {
            LoggedInUser user = new LoggedInUser(input);
            assertThat(user.isAccountNonExpired()).isTrue();
        }

        @Test
        public void shouldCreateLoggedInUserWhoseAccountIsNotLocked() {
            LoggedInUser user = new LoggedInUser(input);
            assertThat(user.isAccountNonLocked()).isTrue();
        }

        @Test
        public void shouldCreateLoggedInUserWhoseCredentialsAreNotExpired() {
            LoggedInUser user = new LoggedInUser(input);
            assertThat(user.isCredentialsNonExpired()).isTrue();
        }

        @Test
        public void shouldCreateLoggedInUserWithCorrectUsername() {
            LoggedInUser user = new LoggedInUser(input);
            assertThat(user.getUsername()).isEqualTo(USERNAME);
        }
    }
}
