package com.testwithspring.intermediate;

import com.testwithspring.intermediate.user.UserRole;

/**
 * This constant class contains the test data that is either
 * found from the DbUnit data set (<em>users.xml</em>) that
 * inserts user data into our database OR that is required
 * to write integration tests for methods that process
 * user data.
 */
public final class Users {

    private Users() {}

    public static final Long ID_NOT_FOUND = 343L;
    public static final String USERNAME_NOT_FOUND = "notfound";

    public static class JohnDoe {

        public static final Long ID = 1L;
        public static final String EMAIL_ADDRESS = "john.doe@gmail.com";
        public static final String NAME = "John Doe";
        public static final String PASSWORD = "user";
        public static final UserRole ROLE = UserRole.ROLE_USER;
    }

    public static class AnneAdmin {

        public static final Long ID = 2L;
        public static final String EMAIL_ADDRESS = "anne.admin@gmail.com";
        public static final String NAME = "Anne Admin";
    }
}
