package com.testwithspring.intermediate.example;

/**
 * Contains the contants that are used by unit and
 * integration tests which use the Spring MVC Test
 * framework.
 */
public final class WebTestConstants {

    private WebTestConstants() {}

    public static class ModelAttributeNames {

        public static final String FORM = "form";
    }

    public static class ModelAttributeProperties {

        public static class Form {

            public static final String CHECKBOX = "checkbox";
            public static final String MESSAGE = "message";
            public static final String NUMBER = "number";
            public static final String RADIO_BUTTON = "radioButton";
        }
    }

    public static class Views {

        public static final String CLICK_SOURCE_VIEW = "click/source";
        public static final String CLICK_TARGET_VIEW = "click/target";
        public static final String EXAMPLE_VIEW = "index";
        public static final String FORM_VIEW = "form/view";
        public static final String FORM_RESULT_VIEW = "form/result";
    }
}
