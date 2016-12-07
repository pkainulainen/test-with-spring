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

    /**
     * This class contains the keys of the used flash messages.
     */
    public static class FlashMessageKey {

        public static final String FEEDBACK_MESSAGE = "feedbackMessage";
    }

    /**
     * This class contains the names of the required model attributes.
     */
    public static class ModelAttributeName {

        public static final String TASK = "task";
        public static final String TASK_ID = "taskId";
        public static final String TASK_LIST = "tasks";
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
}
