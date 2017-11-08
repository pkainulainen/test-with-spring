package com.testwithspring.master.message

import org.springframework.web.servlet.view.InternalResourceViewResolver
import org.springframework.web.servlet.view.JstlView

/**
 * This class provides static factory methods that are used
 * to create the Spring MVC infrastructure components that
 * we use when we are writing unit tests for Spring MVC
 * controllers.
 */
final class WebTestConfig {

    private static final VIEW_BASE_PATH = '/WEB-INF/jsp/'
    private static final VIEW_FILENAME_SUFFIX = '.jsp'

    /**
     * This method creates a view resolver that uses JSP views.
     * @return
     */
    def static jspViewResolver() {
        return new InternalResourceViewResolver(viewClass: JstlView.class,
                prefix: VIEW_BASE_PATH,
                suffix: VIEW_FILENAME_SUFFIX
        )
    }
}
