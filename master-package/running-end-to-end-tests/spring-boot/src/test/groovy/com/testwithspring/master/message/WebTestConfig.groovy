package com.testwithspring.master.message

import org.springframework.web.servlet.view.InternalResourceViewResolver

/**
 * This class provides static factory methods that are used
 * to create the Spring MVC infrastructure components that
 * we use when we are writing unit tests for Spring MVC
 * controllers.
 */
final class WebTestConfig {

    private static final VIEW_BASE_PATH = '/WEB-INF/templates'
    private static final VIEW_FILENAME_SUFFIX = '.html'
    /**
     * This method creates a view resolver that simply returns the name of the view.
     * @return
     */
    def static viewResolver() {
        return new InternalResourceViewResolver(prefix: VIEW_BASE_PATH,
                suffix: VIEW_FILENAME_SUFFIX
        )
    }
}
