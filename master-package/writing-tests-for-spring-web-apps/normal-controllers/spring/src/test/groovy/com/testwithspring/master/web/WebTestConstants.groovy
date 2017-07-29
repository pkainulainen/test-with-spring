package com.testwithspring.master.web

/**
 * This constant class contains the constants that are required by both
 * unit and integration tests which use the Spring MVC Test
 * framework.
 *
 * The idea of this class is to help us to avoid typos, avoid magic strings,
 * and help us to write tests that are easy to maintain.
 */
final class WebTestConstants {


    /**
     * Contains the keys of the flash messages.
     */
    static class FlashMessageKey {

        static final String FEEDBACK_MESSAGE = "feedbackMessage";
    }

    /**
     * Contains the names of the model attributes.
     */
    static class ModelAttributeName {

        static final SEARCH_TERM = 'searchTerm'
        static final TASK_LIST = 'tasks'
    }

    /**
     * Contains the property names of the model attributes.
     */
    static class ModelAttributeProperty {

        static class Task {

            static final ID = 'id'
            static final STATUS = 'status'
            static final TITLE = 'title'
        }
    }

    /**
     * Contains the request parameters.
     */
    static class RequestParameter {

        static final SEARCH_TERM = 'searchTerm'
    }

    /**
     * Contains the view names of the error views.
     */
    static class ErrorView {

        static final NOT_FOUND = "error/404"
    }

    /**
     * Contains the view names of the "normal" views.
     */
    static class View {

        static final LOGIN = 'user/login'
        static final SEARCH_RESULTS = 'task/search-results'
        static final TASK_LIST = 'task/list'
    }

    /**
     * Contains the view names of the redirect views.
     */
    static class RedirectView {

        public static final String SHOW_TASK_LIST = 'redirect:/'
    }
}
