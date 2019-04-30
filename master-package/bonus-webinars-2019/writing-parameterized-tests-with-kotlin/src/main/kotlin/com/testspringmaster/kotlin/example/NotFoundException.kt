package com.testspringmaster.kotlin.example

import java.lang.RuntimeException

/**
 * This exception is thrown when the requested person
 * is not found from the database.
 */
class NotFoundException(message: String): Exception(message)