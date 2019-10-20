package com.testwithspring.master.web

import com.testwithspring.master.common.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Handles the exceptions thrown by our web application.
 */
@ControllerAdvice
open class TaskErrorHandler {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TaskErrorHandler::class.java)
    }

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    open fun handleNotFoundError(ex: NotFoundException) {
        LOGGER.error("Handling exception with message: {}", ex.message)
    }
}