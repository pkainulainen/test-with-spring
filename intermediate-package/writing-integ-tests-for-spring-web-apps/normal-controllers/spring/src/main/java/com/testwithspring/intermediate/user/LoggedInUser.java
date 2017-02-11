package com.testwithspring.intermediate.user;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Contains the information of a logged in user.
 */
public class LoggedInUser implements UserDetails {

    private final Long id;
    private final boolean enabled;
    private final String name;
    private final String password;
    private final UserRole role;
    private final String username;

    LoggedInUser(User user) {
        this.id = user.getId();
        this.enabled = user.isEnabled();
        this.name = user.getName();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.username = user.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.toString()));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("enabled", this.enabled)
                .append("name", this.name)
                .append("username", this.username)
                .append("role", this.role)
                .toString();
    }
}
