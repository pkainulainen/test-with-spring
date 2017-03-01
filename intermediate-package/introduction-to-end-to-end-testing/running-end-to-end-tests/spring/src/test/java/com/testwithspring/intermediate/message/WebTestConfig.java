package com.testwithspring.intermediate.message;

import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * This class provides static factory methods that are used
 * to create the Spring MVC infrastructure components that
 * we use when we are writing unit tests for Spring MVC
 * controllers.
 */
public final class WebTestConfig {

    private static final String VIEW_BASE_PATH = "/WEB-INF/jsp/";
    private static final String VIEW_FILENAME_SUFFIX = ".jsp";

    /**
     * Prevents instantiation.
     */
    private WebTestConfig() {}

    /**
     * This method creates a view resolver that uses JSP views.
     * @return
     */
    public static ViewResolver jspViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix(VIEW_BASE_PATH);
        viewResolver.setSuffix(VIEW_FILENAME_SUFFIX);

        return viewResolver;
    }
}