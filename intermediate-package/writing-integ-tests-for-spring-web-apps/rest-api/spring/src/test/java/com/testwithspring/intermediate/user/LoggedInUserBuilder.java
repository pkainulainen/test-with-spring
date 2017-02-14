package com.testwithspring.intermediate.user;

public final class LoggedInUserBuilder {

    private Long id;

    public LoggedInUserBuilder() {

    }

    public LoggedInUserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public LoggedInUser build() {
        LoggedInUser user = new LoggedInUser();
        user.setId(id);
        return user;
    }
}
