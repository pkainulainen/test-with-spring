package com.testwithspring.master.config

import com.testwithspring.master.user.UserRole
import com.testwithspring.master.web.security.RestAuthenticationFailureHandler
import com.testwithspring.master.web.security.RestAuthenticationSuccessHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import javax.sql.DataSource


@Configuration
@EnableWebSecurity
open class SecurityContext : WebSecurityConfigurerAdapter() {
    @Autowired
    private val dataSource: DataSource? = null

    @Autowired
    private val userDetailsService: UserDetailsService? = null

    /**
     * We use this password encoder here because this is just an example application.
     * DO NOT USE THIS IN A REAL APPLICATION.
     * @return
     */
    @Bean
    open fun passwordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance()
    }

    @Bean
    open fun roleHierarchy(): RoleHierarchy {
        val hierarchy = RoleHierarchyImpl()
        hierarchy.setHierarchy(UserRole.ROLE_ADMIN.toString() + " > " + UserRole.ROLE_USER.toString())
        return hierarchy
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
                .userDetailsService(userDetailsService)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .exceptionHandling()
                .authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureHandler(RestAuthenticationFailureHandler())
                .successHandler(RestAuthenticationSuccessHandler())
                .permitAll()
                .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .logoutUrl("/api/logout")
                .logoutSuccessUrl("/")
                .and()
                .authorizeRequests()
                .anyRequest().hasRole("USER")
    }
}