package com.testwithspring.master

import com.testwithspring.master.task.TaskResolution
import com.testwithspring.master.task.TaskStatus

import java.time.ZonedDateTime

import static com.testwithspring.master.TestDateTimeBuilder.appendOffsetAndZoneIdToDateTime
import static com.testwithspring.master.TestDateTimeBuilder.parseDateTime

final class Tasks {

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
        static final String CREATION_TIME_STRING = appendOffsetAndZoneIdToDateTime('2016-12-03T11:41:28')

        static class Creator {

            static final Long ID = 1L
            static final String NAME = 'John Doe'
        }

        static final String DESCRIPTION = 'This example contains integration tests'
        static final Long ID = 1L
        static final ZonedDateTime MODIFICATION_TIME = parseDateTime('2016-12-03T11:41:28')
        static final String MODIFICATION_TIME_STRING = appendOffsetAndZoneIdToDateTime('2016-12-03T11:41:28')

        static class Modifier {

            static final Long ID = 1L
            static final String NAME = 'John Doe'
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
        static final String CREATION_TIME_STRING = appendOffsetAndZoneIdToDateTime('2016-12-04T11:41:28')

        static class Creator {

            static final Long ID = 1L
            static final String NAME = 'John Doe'
        }

        static final String DESCRIPTION = 'This lesson talks about integration testing'
        static final Long ID = 2L
        static final ZonedDateTime MODIFICATION_TIME =  parseDateTime('2016-12-04T11:41:28')
        static final String MODIFICATION_TIME_STRING = appendOffsetAndZoneIdToDateTime('2016-12-04T11:41:28')

        static class Modifier {

            static final Long ID = 1L
            static final String NAME = 'John Doe'
        }

        static final TaskStatus STATUS = TaskStatus.OPEN
        static final String TITLE = 'Write lesson'

        static class Tags {

            static class Lesson {

                static final Long ID = 2L
                static final String NAME = 'lesson'
            }
        }
    }
}
