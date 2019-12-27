package com.testwithspring.master.web

import com.testwithspring.master.user.LoggedInUser
import com.testwithspring.master.user.UserRole
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

/**
 * This component allows us to configure the user id of the authenticated
 * user that's passed to a tested controller function as a function
 * parameter.
 */
class AuthenticationHandlerMethodArgumentResolver(private val userId: Long): HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType.equals(LoggedInUser::class.java)
    }

    override fun resolveArgument(
            parameter: MethodParameter,
            mavContainer: ModelAndViewContainer,
            webRequest: NativeWebRequest,
            binderFactory: WebDataBinderFactory): Any {
        return LoggedInUser(
                id = userId,
                enabled = true,
                password = "NOT_IMPORTANT",
                role = UserRole.ROLE_USER,
                username = "NOT_IMPORTANT"
        )
    }
}