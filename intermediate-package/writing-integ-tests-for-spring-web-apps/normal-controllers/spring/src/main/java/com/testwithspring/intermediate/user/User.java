package com.testwithspring.intermediate.user;

import com.testwithspring.intermediate.common.AbstractEntity;

import javax.persistence.*;

@Entity
@Table(name = "user_accounts")
class User extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "username", nullable = false)
    private String username;

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
