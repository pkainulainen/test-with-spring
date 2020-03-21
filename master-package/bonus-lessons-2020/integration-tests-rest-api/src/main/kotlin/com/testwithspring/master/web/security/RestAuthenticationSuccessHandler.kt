package com.testwithspring.master.web.security

import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * This authentication success handler returns the information of the authenticated
 * user as JSON.
 */
class RestAuthenticationSuccessHandler : AuthenticationSuccessHandler {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RestAuthenticationSuccessHandler::class.java)
    }

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(request: HttpServletRequest,
                                         response: HttpServletResponse,
                                         authentication: Authentication) {
        LOGGER.info("Authentication was successful")
        response.sendRedirect(response.encodeRedirectURL("/api/authenticated-user"))
    }
}