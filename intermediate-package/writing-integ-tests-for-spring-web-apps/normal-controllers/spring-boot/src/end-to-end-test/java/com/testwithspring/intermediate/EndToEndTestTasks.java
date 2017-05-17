package com.testwithspring.intermediate;

import com.testwithspring.intermediate.task.TaskResolution;
import com.testwithspring.intermediate.task.TaskStatus;

/**
 * This constant class contains the test data that is inserted into
 * our database before our web application is started.
 */
public final class EndToEndTestTasks {

    private EndToEndTestTasks() {}

    public static final Long NEW_TASK_ID = 3L;

    public static final Long TASK_ID_NOT_FOUND = 599L;
    public static final String SEARCH_TERM_ONE_MATCH = "Esso";
    public static final String SEARCH_TERM_TWO_MATCHES = "This";
    public static final String SEARCH_TERM_NOT_FOUND = "NO_PE";

    public static class WriteExampleApp {

        public static class Assignee {

            public static final Long ID = 1L;
            public static final String NAME = "John Doe";
        }

        public static class Closer {

            public static final Long ID = 1L;
            public static final String NAME = "John Doe";
        }

        public static class Creator {

            public static final Long ID = 1L;
            public static final String NAME = "John Doe";
        }

        public static final String DESCRIPTION = "This example contains end-to-end tests";
        public static final Long ID = 1L;

        public static class Modifier {

            public static final Long ID = 1L;
            public static final String NAME = "John Doe";
        }

        public static final TaskResolution RESOLUTION = TaskResolution.DONE;
        public static final TaskStatus STATUS = TaskStatus.CLOSED;
        public static final String TITLE = "Write example application";

        public static class Tags {

            public static class Example {

                public static final Long ID = 1L;
                public static final String NAME = "example";
            }
        }
    }

    public static class WriteLesson {

        public static class Creator {

            public static final String NAME = "John Doe";
        }

        public static final String DESCRIPTION = "This lesson talks about end-to-end testing";
        public static final Long ID = 2L;

        public static final TaskStatus STATUS = TaskStatus.OPEN;
        public static final String TITLE = "Write lesson";

        public static class Tags {

            public static class Lesson {

                public static final Long ID = 1L;
                public static final String NAME = "lesson";
            }
        }
    }
}
