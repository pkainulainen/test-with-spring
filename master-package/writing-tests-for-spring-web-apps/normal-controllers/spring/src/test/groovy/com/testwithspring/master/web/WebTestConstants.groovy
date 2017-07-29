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
     * Contains the view names of the "normal" views.
     */
    static class View {

        static final LOGIN = 'user/login'
        static final SEARCH_RESULTS = 'task/search-results'
    }
}
