package com.testwithspring.master.web.security

import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * This authentication failure handler returns the HTTP status code 403.
 */
class RestAuthenticationFailureHandler : AuthenticationFailureHandler {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RestAuthenticationFailureHandler::class.java)
    }

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(request: HttpServletRequest,
                                         response: HttpServletResponse,
                                         e: AuthenticationException) {
        LOGGER.info("Authentication failed with message: {}", e.message)
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authentication failed.")
    }
}