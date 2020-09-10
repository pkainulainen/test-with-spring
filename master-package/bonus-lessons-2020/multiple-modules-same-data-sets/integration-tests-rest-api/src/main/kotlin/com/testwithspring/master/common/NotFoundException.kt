package com.testwithspring.master.common

/**
 * This exception is thrown when the requested information
 * is not found from the database.
 */
class NotFoundException(message: String) : RuntimeException(message)