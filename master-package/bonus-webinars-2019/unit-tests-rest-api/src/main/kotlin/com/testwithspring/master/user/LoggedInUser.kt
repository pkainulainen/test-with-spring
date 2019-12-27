package com.testwithspring.master.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * Specifies the user roles that can assigned to a user.
 */
enum class UserRole {
    ROLE_USER, ROLE_ADMIN
}

/**
 * Contains the infiormation of an authenticated user.
 */
class LoggedInUser(
        val id: Long,
        private val enabled: Boolean,
        private val password: String,
        val role: UserRole,
        private val username: String
): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(SimpleGrantedAuthority(role.toString()))

    override fun isEnabled(): Boolean = enabled

    override fun getUsername(): String = username

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String = password

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true
}