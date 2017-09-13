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

    static String LOGIN_PAGE_URL = 'http://localhost/user/login'

    /**
     * Contains the keys of the flash messages.
     */
    static class FlashMessageKey {

        static final FEEDBACK_MESSAGE = 'feedbackMessage'
    }

    /**
     * Contains the names of the model attributes.
     */
    static class ModelAttributeName {

        static final SEARCH_TERM = 'searchTerm'
        static final TASK = 'task'
        static final TASK_ID = 'taskId'
        static final TASK_LIST = 'tasks'
    }

    /**
     * Contains the property names of the model attributes.
     */
    static class ModelAttributeProperty {

        static class Tag {

            static final ID = 'id'
            static final NAME = 'name'
        }

        static class Task {

            static final ASSIGNEE = 'assignee'
            static final CLOSER = 'closer'
            static final CREATION_TIME = 'creationTime'
            static final CREATOR = 'creator'
            static final DESCRIPTION = 'description'
            static final ID = 'id'
            static final MODIFICATION_TIME = 'modificationTime'
            static final MODIFIER = 'modifier'
            static final RESOLUTION = 'resolution'
            static final STATUS = 'status'
            static final TAGS = 'tags'
            static final TITLE = 'title'

            static class Person {

                static final NAME = 'name'
                static final USER_ID = 'userId'
            }
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

        static final NOT_FOUND = 'error/404'
    }

    /**
     * Contains the validation error codes.
     */
    static class ValidationErrorCode {

        static final EMPTY_FIELD = 'NotBlank'
        static final SIZE = 'Size'
    }

    /**
     * Contains the view names of the 'normal' views.
     */
    static class View {

        static final CREATE_TASK = 'task/create'
        static final LOGIN = 'user/login'
        static final SEARCH_RESULTS = 'task/search-results'
        static final TASK_LIST = 'task/list'
        static final UPDATE_TASK = 'task/update'
        static final VIEW_TASK = 'task/view'
    }

    /**
     * Contains the view names of the redirect views.
     */
    static class RedirectView {

        static final SHOW_TASK = 'redirect:/task/{taskId}'
        static final SHOW_TASK_LIST = 'redirect:/'
    }
}
