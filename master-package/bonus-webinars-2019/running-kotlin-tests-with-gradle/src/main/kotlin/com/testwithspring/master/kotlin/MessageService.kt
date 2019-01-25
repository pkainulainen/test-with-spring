package com.testwithspring.master.kotlin

/**
 * Thiis class is simply used to ensure that our
 * unit and integration tests are run correctly.
 */
class MessageService {

    /**
     * Return a hardcoded message.
     * @return  The message: 'Hello World!'
     */
    fun getMessage(): String {
        return "Hello World!"
    }
}