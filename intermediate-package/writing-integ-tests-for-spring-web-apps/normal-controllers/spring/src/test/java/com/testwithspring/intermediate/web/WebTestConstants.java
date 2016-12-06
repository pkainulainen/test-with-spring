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
     * This class contains the required model attributes.
     */
    public static class ModelAttributes {

        public static String TASK = "task";
        public static String TASK_ID = "taskId";
        public static String TASK_LIST = "tasks";
    }
}
