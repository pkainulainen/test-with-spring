package com.testwithspring.master

import com.testwithspring.master.user.UserRole

/**
 * This constant class contains the test data that is either
 * found from the DbUnit data set (<em>users.xml</em>) that
 * inserts user data into our database OR that is required
 * to write integration tests for methods that process
 * user data.
 */
class Users {

    companion object {

        const val EMAIL_ADDRESS_NOT_FOUND = "unknown@gmail.com";
        const val ID_NOT_FOUND = 343L
    }

    class JohnDoe {

        companion object {
            const val ID = 1L
            const val EMAIL_ADDRESS = "john.doe@gmail.com"
            const val NAME = "John Doe"
            const val PASSWORD = "user"
            val ROLE = UserRole.ROLE_USER
        }
    }

    class AnneAdmin {

        companion object {

            const val ID = 2L
            const val EMAIL_ADDRESS = "anne.admin@gmail.com"
            const val NAME = "Anne Admin"
        }
    }
}