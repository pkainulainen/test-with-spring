package com.testwithspring.intermediate.user;

import com.testwithspring.intermediate.ReflectionFieldUtil;

/**
 * This class creates new {@code User} objects by setting
 * the field values of the created objects with reflection.
 * The reason why we use this class is that we don't want
 * to add unnecessary methods to the {@code User} class until
 * we really need them.
 *
 * Also, this class helps us to hide our "ugly hack" behind
 * a single API.
 */
final class UserBuilder {

    private String emailAddress;
    private boolean enabled;
    private Long id;
    private String name;
    private String password;
    private UserRole role;

    UserBuilder() {

    }

    UserBuilder isEnabled() {
        this.enabled = true;
        return this;
    }

    UserBuilder withEmailAddress(String username) {
        this.emailAddress = username;
        return this;
    }

    UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    UserBuilder withRoleUser() {
        this.role = UserRole.ROLE_USER;
        return this;
    }

    User build() {
        User user = new User();

        ReflectionFieldUtil.setFieldValue(user, "emailAddress", emailAddress);
        ReflectionFieldUtil.setFieldValue(user, "enabled", enabled);
        ReflectionFieldUtil.setFieldValue(user, "id", id);
        ReflectionFieldUtil.setFieldValue(user, "name", name);
        ReflectionFieldUtil.setFieldValue(user, "password", password);
        ReflectionFieldUtil.setFieldValue(user, "role", role);

        return user;
    }
}
