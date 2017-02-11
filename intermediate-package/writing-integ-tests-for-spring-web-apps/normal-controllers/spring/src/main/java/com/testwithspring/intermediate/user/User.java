package com.testwithspring.intermediate.user;

import com.testwithspring.intermediate.common.AbstractEntity;

import javax.persistence.*;

@Entity
@Table(name = "user_accounts")
class User extends AbstractEntity {

    @Column(name = "is_enabled", nullable = false)
    private boolean enabled;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "username", nullable = false)
    private String username;

    boolean isEnabled() {
        return enabled;
    }

    String getName() {
        return name;
    }

    String getPassword() {
        return password;
    }

    UserRole getRole() {
        return role;
    }

    String getUsername() {
        return username;
    }
}
