package com.testwithspring.intermediate;

import com.testwithspring.intermediate.task.TaskResolution;
import com.testwithspring.intermediate.task.TaskStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class Tasks {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    private Tasks() {}

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

        public static final ZonedDateTime CREATION_TIME = parseDateTime("2016-12-03T11:41:28");
        public static final String CREATION_TIME_STRING = getDateTimeWithZoneOffsetAndId("2016-12-03T11:41:28");

        public static class Creator {

            public static final Long ID = 1L;
            public static final String NAME = "John Doe";
        }

        public static final String DESCRIPTION = "This example contains integration tests";
        public static final Long ID = 1L;
        public static final ZonedDateTime MODIFICATION_TIME = parseDateTime("2016-12-03T11:41:28");
        public static final String MODIFICATION_TIME_STRING = getDateTimeWithZoneOffsetAndId("2016-12-03T11:41:28");

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

        public static final ZonedDateTime CREATION_TIME =  parseDateTime("2016-12-04T11:41:28");
        public static final String CREATION_TIME_STRING = getDateTimeWithZoneOffsetAndId("2016-12-04T11:41:28");

        public static class Creator {

            public static final Long ID = 1L;
            public static final String NAME = "John Doe";
        }

        public static final String DESCRIPTION = "This lesson talks about integration testing";
        public static final Long ID = 2L;
        public static final ZonedDateTime MODIFICATION_TIME =  parseDateTime("2016-12-04T11:41:28");
        public static final String MODIFICATION_TIME_STRING = getDateTimeWithZoneOffsetAndId("2016-12-04T11:41:28");

        public static class Modifier {

            public static final Long ID = 1L;
            public static final String NAME = "John Doe";
        }

        public static final TaskStatus STATUS = TaskStatus.OPEN;
        public static final String TITLE = "Write lesson";

        public static class Tags {

            public static class Lesson {

                public static final Long ID = 2L;
                public static final String NAME = "lesson";
            }
        }
    }

    private static ZonedDateTime parseDateTime(String dateTime) {
        String dateTimeWithZone  = getDateTimeWithZoneOffsetAndId(dateTime);
        return ZonedDateTime.from(DATE_TIME_FORMAT.parse(dateTimeWithZone));
    }

    private static String getDateTimeWithZoneOffsetAndId(String dateTime) {
        dateTime += getSystemZoneOffset();
        dateTime += getSystemZoneId();
        return dateTime;
    }

    private static String getSystemZoneOffset() {
        return ZonedDateTime.now().getOffset().toString();
    }

    private static String getSystemZoneId() {
        return "[" + ZoneId.systemDefault().toString() + "]";
    }
}
