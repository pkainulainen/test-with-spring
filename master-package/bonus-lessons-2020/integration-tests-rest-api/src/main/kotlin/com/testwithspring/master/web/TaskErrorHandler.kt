package com.testwithspring.master.web

import com.testwithspring.master.common.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
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

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    open fun handleValidationError(ex: MethodArgumentNotValidException): ValidationErrorDTO {
        val result = ex.bindingResult
        val fieldErrors = result.fieldErrors
        return processFieldErrors(fieldErrors)
    }

    private fun processFieldErrors(fieldErrors: List<FieldError>): ValidationErrorDTO {
        val dto = ValidationErrorDTO()
        for (fieldError in fieldErrors) {
            val errorCode = resolveErrorCode(fieldError)
            dto.addFieldError(fieldError.field, errorCode)
        }
        return dto
    }

    private fun resolveErrorCode(fieldError: FieldError): String {
        val fieldErrorCodes = fieldError.codes
        return fieldErrorCodes?.get(fieldErrorCodes.size - 1) ?: "UnknownError"
    }

}