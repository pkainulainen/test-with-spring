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

    private Long id;
    private boolean enabled;
    private String name;
    private String password;
    private UserRole role;
    private String username;

    public LoggedInUser() {
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

    void setId(Long id) {
        this.id = id;
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    void setName(String name) {
        this.name = name;
    }

    void setPassword(String password) {
        this.password = password;
    }

    void setRole(UserRole role) {
        this.role = role;
    }

    void setUsername(String username) {
        this.username = username;
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
