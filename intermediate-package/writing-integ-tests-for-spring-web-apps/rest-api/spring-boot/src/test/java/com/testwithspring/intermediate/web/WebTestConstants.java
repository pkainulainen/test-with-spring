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
     * This class contains the property names of the JSON documents.
     */
    public static class JsonPathProperty {

        public static class Task {

            public static String ASSIGNEE = "$.assignee";
            public static class Assignee {

                public static String ID = "$.assignee.userId";
                public static String NAME = "$.assignee.name";
            }

            public static String CLOSER = "$.closer";
            public static class Closer {

                public static String ID = "$.closer.userId";
                public static String NAME = "$.closer.name";
            }

            public static final String CREATION_TIME = "$.creationTime";

            public static class Creator {

                public static String ID = "$.creator.userId";
                public static String NAME = "$.creator.name";
            }

            public static final String DESCRIPTION = "$.description";
            public static final String ID = "$.id";
            public static final String MODIFICATION_TIME = "$.modificationTime";

            public static class Modifier {

                public static String ID = "$.modifier.userId";
                public static String NAME = "$.modifier.name";
            }

            public static final String RESOLUTION = "$.resolution";
            public static final String STATUS = "$.status";
            public static final String TAGS = "$.tags";
            public static final String TITLE = "$.title";
        }
    }

    /**
     * This class contains the required validation error codes.
     */
    public class ValidationErrorCode {

        public static final String EMPTY_FIELD = "NotBlank";
        public static final String TOO_LONG_FIELD = "Size";
    }

    public static class RequestParameter {

        public static final String SEARCH_TERM = "searchTerm";
    }
}
