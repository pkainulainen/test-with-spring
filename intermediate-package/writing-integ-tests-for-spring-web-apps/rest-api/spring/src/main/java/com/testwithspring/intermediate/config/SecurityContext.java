package com.testwithspring.intermediate.config;

import com.testwithspring.intermediate.user.UserRole;
import com.testwithspring.intermediate.web.security.CSRFHeaderFilter;
import com.testwithspring.intermediate.web.security.RestAuthenticationFailureHandler;
import com.testwithspring.intermediate.web.security.RestAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityContext extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * We use this password encoder here because this is just an example application.
     * DO NOT USE THIS IN A REAL APPLICATION.
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    RoleHierarchy roleHierarchy() {
        final RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy(UserRole.ROLE_ADMIN.toString() + " > " + UserRole.ROLE_USER.toString());
        return hierarchy;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                    .and()
                .formLogin()
                    .loginProcessingUrl("/api/login")
                    .failureHandler(new RestAuthenticationFailureHandler())
                    .successHandler(new RestAuthenticationSuccessHandler())
                    .permitAll()
                    .and()
                .logout()
                    .deleteCookies("JSESSIONID")
                    .logoutUrl("/api/logout")
                    .logoutSuccessUrl("/")
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/api/csrf"
                ).permitAll()
                .anyRequest().hasRole("USER")
                .and()
                .addFilterAfter(new CSRFHeaderFilter(), CsrfFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                //Spring Security ignores request to static resources such as CSS or JS files.
                .ignoring()
                .antMatchers(
                        "/favicon.ico",
                        "/css/**",
                        "/i18n/**",
                        "/js/**"
                );
    }
}
