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

        val EMAIL_ADDRESS_NOT_FOUND = "unknown@gmail.com";
        val ID_NOT_FOUND = 343L
    }

    class JohnDoe {

        companion object {
            val ID = 1L
            val EMAIL_ADDRESS = "john.doe@gmail.com"
            val NAME = "John Doe"
            val PASSWORD = "user"
            val ROLE = UserRole.ROLE_USER
        }
    }

    class AnneAdmin {

        companion object {

            val ID = 2L
            val EMAIL_ADDRESS = "anne.admin@gmail.com"
            val NAME = "Anne Admin"
        }
    }
}