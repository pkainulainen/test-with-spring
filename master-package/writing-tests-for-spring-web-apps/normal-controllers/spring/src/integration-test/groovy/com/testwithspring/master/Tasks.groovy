package com.testwithspring.master

import com.testwithspring.master.task.TaskResolution
import com.testwithspring.master.task.TaskStatus

import java.time.ZonedDateTime

import static com.testwithspring.master.TestDateTimeBuilder.parseDateTime

/**
 * This constant class contains the test data that is either
 * found from the DbUnit data sets (<em>tasks.xml</em> and <em>task.xml</em>)
 * that insert task data into our database OR that is required
 * to write integration tests for methods that process
 * task data.
 */
class Tasks {

    static final Long TASK_ID_NOT_FOUND = 599L
    static final String SEARCH_TERM_ONE_MATCH = 'Esso'
    static final String SEARCH_TERM_TWO_MATCHES = 'This'
    static final String SEARCH_TERM_NOT_FOUND = 'NO_PE'

    static class WriteExampleApp {

        static class Assignee {

            static final Long ID = 1L
            static final String NAME = 'John Doe'
        }

        static class Closer {

            static final Long ID = 1L
            static final String NAME = 'John Doe'
        }

        static final ZonedDateTime CREATION_TIME = parseDateTime('2016-12-03T11:41:28')

        static class Creator {

            static final Long ID = 1L
            static final String NAME = 'John Doe'
        }

        static final String DESCRIPTION = 'This example contains integration tests'
        static final Long ID = 1L
        static final ZonedDateTime MODIFICATION_TIME = parseDateTime('2016-12-03T11:41:28')

        static class Modifier {

            static final Long ID = 1L
            static final String MODIFIER = 'John Doe'
        }

        static final TaskResolution RESOLUTION = TaskResolution.DONE
        static final TaskStatus STATUS = TaskStatus.CLOSED
        static final String TITLE = 'Write example application'

        static class Tags {

            static class Example {

                static final Long ID = 1L
                static final String NAME = 'example'
            }
        }
    }

    static class WriteLesson {

        static final ZonedDateTime CREATION_TIME =  parseDateTime('2016-12-04T11:41:28')
        static final Long CREATOR_ID = 1L
        static final String DESCRIPTION = 'This lesson talks about integration testing'
        static final Long ID = 2L
        static final ZonedDateTime MODIFICATION_TIME =  parseDateTime('2016-12-04T11:41:28')
        static final TaskStatus STATUS = TaskStatus.OPEN
        static final String TITLE = 'Write lesson'

        static class Tags {

            static class Lesson {

                static final Long ID = 1L
                static final String NAME = 'lesson'
            }
        }
    }
}
