package com.testwithspring.intermediate.user;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.testwithspring.intermediate.IntegrationTest;
import com.testwithspring.intermediate.IntegrationTestContext;
import com.testwithspring.intermediate.ReplacementDataSetLoader;
import com.testwithspring.intermediate.Users;
import com.testwithspring.intermediate.config.Profiles;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {IntegrationTestContext.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@DatabaseSetup({
        "/com/testwithspring/intermediate/users.xml",
        "/com/testwithspring/intermediate/no-tasks-and-tags.xml"
})
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
public class LoadUserByUsernameWhenUserIsFound {

    @Autowired
    private UserDetailsService service;

    @Test
    public void shouldReturnFoundUserWithCorrectUserInformation() {
        LoggedInUser user = (LoggedInUser) service.loadUserByUsername(Users.JohnDoe.EMAIL_ADDRESS);

        assertThat(user.getId()).isEqualTo(Users.JohnDoe.ID);
        assertThat(user.getName()).isEqualTo(Users.JohnDoe.NAME);
        assertThat(user.getPassword()).isEqualTo(Users.JohnDoe.PASSWORD);
        assertThat(user.getUsername()).isEqualTo(Users.JohnDoe.EMAIL_ADDRESS);
    }

    @Test
    public void shouldReturnFoundUserWhoCanBeAuthenticated() {
        LoggedInUser user = (LoggedInUser) service.loadUserByUsername(Users.JohnDoe.EMAIL_ADDRESS);

        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
        assertThat(user.isEnabled()).isTrue();
        assertThat(user.isCredentialsNonExpired()).isTrue();
    }

    @Test
    public void shouldReturnUserWithCorrectGrantedAuthority() {
        LoggedInUser user = (LoggedInUser) service.loadUserByUsername(Users.JohnDoe.EMAIL_ADDRESS);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertThat(authorities).hasSize(1);

        GrantedAuthority authority = authorities.iterator().next();
        assertThat(authority.getAuthority()).isEqualTo(Users.JohnDoe.ROLE.toString());
    }
}
