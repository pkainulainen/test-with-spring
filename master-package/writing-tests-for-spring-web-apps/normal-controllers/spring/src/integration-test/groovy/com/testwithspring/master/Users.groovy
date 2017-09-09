package com.testwithspring.master

import com.testwithspring.master.user.UserRole

/**
 * This constant class contains the test data that is either
 * found from the DbUnit data set (<em>users.xml</em>) that
 * inserts user data into our database OR that is required
 * to write integration tests for methods that process
 * user data.
 */
final class Users {

    static final Long ID_NOT_FOUND = 343L
    static final String USERNAME_NOT_FOUND = 'notfound'

    static class JohnDoe {

        static final Long ID = 1L
        static final String EMAIL_ADDRESS = 'john.doe@gmail.com'
        static final String NAME = 'John Doe'
        static final String PASSWORD = 'user'
        static final UserRole ROLE = UserRole.ROLE_USER
    }

    static class AnneAdmin {

        static final Long ID = 2L
        static final String EMAIL_ADDRESS = 'anne.admin@gmail.com'
        static final String NAME = 'Anne Admin'
    }
}
