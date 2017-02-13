package com.testwithspring.intermediate.web;

import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Locale;
import java.util.Properties;

/**
 * This class provides static factory methods that are used
 * to create the Spring MVC infrastructure components that
 * we use when we are writing unit tests for Spring MVC
 * controllers.
 */
public final class WebTestConfig {

    private static final String HTTP_STATUS_CODE_INTERNAL_SERVER_ERROR = "500";
    private static final String HTTP_STATUS_CODE_NOT_FOUND = "404";

    private static final String VIEW_BASE_PATH = "/WEB-INF/templates";
    private static final String VIEW_FILENAME_SUFFIX = ".html";
    private static final String VIEW_NAME_ERROR_VIEW = "error/error";
    private static final String VIEW_NAME_NOT_FOUND_VIEW = "error/404";

    public static final Locale LOCALE = Locale.ENGLISH;

    /**
     * Prevents instantiation.
     */
    private WebTestConfig() {}

    /**
     * This method returns an exception resolver that maps exceptions to the error view names and
     * resolves the returned HTTP status code of the rendered error view.
     *
     * @return
     */
    public static SimpleMappingExceptionResolver exceptionResolver() {
        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();

        Properties exceptionMappings = new Properties();

        exceptionMappings.put("com.testwithspring.intermediate.common.NotFoundException", VIEW_NAME_NOT_FOUND_VIEW);
        exceptionMappings.put("java.lang.Exception", VIEW_NAME_ERROR_VIEW);
        exceptionMappings.put("java.lang.RuntimeException", VIEW_NAME_ERROR_VIEW);

        exceptionResolver.setExceptionMappings(exceptionMappings);

        Properties statusCodes = new Properties();

        statusCodes.put(VIEW_NAME_NOT_FOUND_VIEW, HTTP_STATUS_CODE_NOT_FOUND);
        statusCodes.put(VIEW_NAME_ERROR_VIEW, HTTP_STATUS_CODE_INTERNAL_SERVER_ERROR);

        exceptionResolver.setStatusCodes(statusCodes);

        return exceptionResolver;
    }

    /**
     * This method creates a locale resolver that always returns {@code Locale.ENGLISH}
     * @return
     */
    public static LocaleResolver fixedLocaleResolver() {
        return new FixedLocaleResolver(LOCALE);
    }

    /**
     * This method creates a view resolver that simply returns the name of the view.
     * @return
     */
    public static ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setPrefix(VIEW_BASE_PATH);
        viewResolver.setSuffix(VIEW_FILENAME_SUFFIX);

        return viewResolver;
    }
}
