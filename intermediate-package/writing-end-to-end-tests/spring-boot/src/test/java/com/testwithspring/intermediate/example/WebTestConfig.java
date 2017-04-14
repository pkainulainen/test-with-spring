package com.testwithspring.intermediate.example;

import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

public final class WebTestConfig {

    private static final String VIEW_BASE_PATH = "/WEB-INF/templates";
    private static final String VIEW_FILENAME_SUFFIX = ".html";

    /**
     * Prevents instantiation.
     */
    private WebTestConfig() {}

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


