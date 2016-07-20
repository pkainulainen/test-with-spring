package com.testwithspring.user;

public class LoggedInUser {

    private final Long userId;
    private final String username;

    public LoggedInUser(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
