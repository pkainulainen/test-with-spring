package com.testwithspring.master.web

import java.util.*

/**
 * Contains the information of a single validation error.
 */
data class FieldErrorDTO(val field: String, val errorCode: String)

/**
 * Contains all validation errors which occurred during
 * validation.
 */
class ValidationErrorDTO {

    private val fieldErrors = ArrayList<FieldErrorDTO>()

    fun addFieldError(path: String, errorCode: String) {
        val error = FieldErrorDTO(path, errorCode)
        fieldErrors.add(error)
    }

    fun getFieldErrors(): List<FieldErrorDTO> {
        return fieldErrors
    }
}