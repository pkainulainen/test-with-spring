package com.testwithspring.intermediate.web;

/**
 * This constant class contains the constants that are required by both
 * unit and integration tests which use the Spring MVC Test
 * framework.
 *
 * The idea of this class is to help us to avoid typos, avoid magic strings,
 * and help us to write tests that are easy to maintain.
 */
public final class WebTestConstants {

    private WebTestConstants() {}

    public static String LOGIN_PAGE_URL = "http://localhost/user/login";

    /**
     * This class contains the keys of the used flash messages.
     */
    public static class FlashMessageKey {

        public static final String FEEDBACK_MESSAGE = "feedbackMessage";
    }

    /**
     * This class contains the property names of the required model attributes.
     */
    public static class ModelAttributeProperty {

        public static class Tag {

            public static final String ID = "id";
            public static final String NAME = "name";
        }

        public static class Task {

            public static final String ASSIGNEE = "assignee";
            public static final String CLOSER = "closer";
            public static final String CREATION_TIME = "creationTime";
            public static final String CREATOR = "creator";
            public static final String DESCRIPTION = "description";
            public static final String ID = "id";
            public static final String MODIFICATION_TIME = "modificationTime";
            public static final String MODIFIER = "modifier";
            public static final String RESOLUTION = "resolution";
            public static final String STATUS = "status";
            public static final String TAGS = "tags";
            public static final String TITLE = "title";

            public static class Person {

                public static final String NAME = "name";
                public static final String USER_ID = "userId";
            }
        }
    }

    /**
     * This class contains the names of the required model attributes.
     */
    public static class ModelAttributeName {

        public static final String TASK = "task";
        public static final String TASK_ID = "taskId";
        public static final String TASK_LIST = "tasks";
        public static final String SEARCH_TERM = "searchTerm";
    }

    /**
     * This class contains the view names of the required error views.
     */
    public static class ErrorView {

        public static final String NOT_FOUND = "error/404";
    }

    /**
     * This class contains the required validation error codes.
     */
    public class ValidationErrorCode {

        public static final String EMPTY_FIELD = "NotBlank";
    }

    /**
     * This class contains the view names of the required "normal" views.
     */
    public static class View {

        public static final String CREATE_TASK = "task/create";
        public static final String SEARCH_RESULTS = "task/search-results";
        public static final String TASK_LIST = "task/list";
        public static final String UPDATE_TASK = "task/update";
        public static final String VIEW_TASK = "task/view";
    }

    /**
     * This class contains the view names of the used redirect views.
     */
    public static class RedirectView {

        public static final String SHOW_TASK = "redirect:/task/{taskId}";
        public static final String SHOW_TASK_LIST = "redirect:/";
    }

    public static class RequestParameter {

        public static final String SEARCH_TERM = "searchTerm";
    }
}
