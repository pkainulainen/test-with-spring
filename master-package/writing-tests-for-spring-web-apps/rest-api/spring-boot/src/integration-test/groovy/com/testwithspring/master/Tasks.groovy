package com.testwithspring.master

import com.testwithspring.master.task.TaskResolution
import com.testwithspring.master.task.TaskStatus

final class Tasks {

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

        static final CREATION_TIME = TestDateTimeBuilder.parseLocalDateTimeFromUTCDateTime('2016-12-03T11:41:28')
        static final CREATION_TIME_STRING = TestDateTimeBuilder.transformUTCDateToLocalDateTime('2016-12-03T11:41:28')

        static class Creator {

            static final ID = 1L
            static final NAME = 'John Doe'
        }

        static final DESCRIPTION = 'This example contains integration tests'
        static final ID = 1L
        static final MODIFICATION_TIME = TestDateTimeBuilder.parseLocalDateTimeFromUTCDateTime('2016-12-03T11:41:28')
        static final MODIFICATION_TIME_STRING = TestDateTimeBuilder.transformUTCDateToLocalDateTime('2016-12-03T11:41:28')

        static class Modifier {

            static final ID = 1L
            static final NAME = 'John Doe'
        }

        static final RESOLUTION = TaskResolution.DONE
        static final STATUS = TaskStatus.CLOSED
        static final TITLE = 'Write example application'

        static class Tags {

            static class Example {

                static final ID = 1L
                static final NAME = 'example'
            }
        }
    }

    static class WriteLesson {

        static final CREATION_TIME =  TestDateTimeBuilder.parseLocalDateTimeFromUTCDateTime('2016-12-04T11:41:28')
        static final CREATION_TIME_STRING = TestDateTimeBuilder.transformUTCDateToLocalDateTime('2016-12-04T11:41:28')

        static class Creator {

            static final ID = 1L
            static final NAME = 'John Doe'
        }

        static final DESCRIPTION = 'This lesson talks about integration testing'
        static final ID = 2L
        static final MODIFICATION_TIME =  TestDateTimeBuilder.parseLocalDateTimeFromUTCDateTime('2016-12-04T11:41:28')
        static final MODIFICATION_TIME_STRING = TestDateTimeBuilder.transformUTCDateToLocalDateTime('2016-12-04T11:41:28')

        static class Modifier {

            static final ID = 1L
            static final NAME = 'John Doe'
        }

        static final STATUS = TaskStatus.OPEN
        static final TITLE = 'Write lesson'

        static class Tags {

            static class Lesson {

                static final ID = 2L
                static final NAME = 'lesson'
            }
        }
    }
}
