package com.testwithspring.master

/**
 * This constant class contains the user data that is either inserted
 * into our database (see the {@code users.sql} file) or used by
 * end-to-end tests.
 */
class EndToEndTestUsers {
    
    static class JohnDoe {

        static final EMAIL_ADDRESS = 'john.doe@gmail.com'
        static final NAME = 'John Doe'
        static final PASSWORD = 'user'
    }

    static class UnknownUser {

        static final EMAIL_ADDRESS = 'not.found@gmail.com'
        static final PASSWORD = 'notfound'
    }
}
