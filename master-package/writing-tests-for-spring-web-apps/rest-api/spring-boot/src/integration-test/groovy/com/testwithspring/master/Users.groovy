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

    static final ID_NOT_FOUND = 343L
    static final USERNAME_NOT_FOUND = 'notfound'

    static class JohnDoe {

        static final ID = 1L
        static final EMAIL_ADDRESS = 'john.doe@gmail.com'
        static final NAME = 'John Doe'
        static final PASSWORD = 'user'
        static final ROLE = UserRole.ROLE_USER
    }

    static class AnneAdmin {

        static final ID = 2L
        static final EMAIL_ADDRESS = 'anne.admin@gmail.com'
        static final NAME = 'Anne Admin'
    }
}
