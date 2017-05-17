package com.testwithspring.intermediate;

public final class EndToEndTestUsers {

    private EndToEndTestUsers() {}

    public static class JohnDoe {

        public static final String EMAIL_ADDRESS = "john.doe@gmail.com";
        public static final String NAME = "John Doe";
        public static final String PASSWORD = "user";
    }

    public static class UnknownUser {

        public static final String EMAIL_ADDRESS = "not.found@gmail.com";
        public static final String PASSWORD = "notfound";
    }
}
