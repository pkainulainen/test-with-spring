package com.testwithspring.master

import com.testwithspring.master.task.TaskResolution
import com.testwithspring.master.task.TaskStatus

/**
 * This constant class contains the test data that is found from
 * the {@code tasks.sql} file which inserts task data into our
 * database OR that is required to write end-to-end tests for methods
 * that process task data.
 */
class EndToEndTestTasks {
        
    static final NEW_TASK_ID = 3L

    static final TASK_ID_NOT_FOUND = 599L
    static final SEARCH_TERM_ONE_MATCH = 'Esso'
    static final SEARCH_TERM_TWO_MATCHES = 'This'
    static final SEARCH_TERM_NOT_FOUND = 'NO_PE'

    static class WriteExampleApp {

        static class Assignee {

            static final ID = 1L
            static final NAME = 'John Doe'
        }

        static class Closer {

            static final ID = 1L
            static final NAME = 'John Doe'
        }

        static class Creator {

            static final ID = 1L
            static final NAME = 'John Doe'
        }

        static final DESCRIPTION = 'This example contains end-to-end tests'
        static final ID = 1L

        static class Modifier {

            static final ID = 1L
            static final NAME = 'John Doe'
        }

        static final TaskResolution RESOLUTION = TaskResolution.DONE
        static final TaskStatus STATUS = TaskStatus.CLOSED
        static final TITLE = 'Write example application'

        static class Tags {

            static class Example {

                static final ID = 1L
                static final NAME = 'example'
            }
        }
    }

    static class WriteLesson {

        static class Creator {

            static final NAME = 'John Doe'
        }

        static final DESCRIPTION = 'This lesson talks about end-to-end testing'
        static final ID = 2L

        static final TaskStatus STATUS = TaskStatus.OPEN
        static final TITLE = 'Write lesson'

        static class Tags {

            static class Lesson {

                static final ID = 1L
                static final NAME = 'lesson'
            }
        }
    }
}
