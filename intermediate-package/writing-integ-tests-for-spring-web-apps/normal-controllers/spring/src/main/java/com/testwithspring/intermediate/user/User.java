package com.testwithspring.intermediate.user;

import com.testwithspring.intermediate.common.AbstractEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name = "user_accounts")
class User extends AbstractEntity {

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @Column(name = "is_enabled", nullable = false)
    private boolean enabled;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

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

    String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.getId())
                .append("creationTime", this.getCreationTime())
                .append("emailAddress", this.emailAddress)
                .append("enabled", this.enabled)
                .append("modificationTime", this.getModificationTime())
                .append("name", this.name)
                .append("role", this.role)
                .append("version", this.getVersion())
                .toString();
    }
}
