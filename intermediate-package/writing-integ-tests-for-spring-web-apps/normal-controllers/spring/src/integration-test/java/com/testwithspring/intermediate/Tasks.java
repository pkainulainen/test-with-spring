package com.testwithspring.intermediate;

import com.testwithspring.intermediate.task.TaskResolution;
import com.testwithspring.intermediate.task.TaskStatus;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class Tasks {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private Tasks() {}

    public static final Long TASK_ID_NOT_FOUND = 599L;
    public static final String SEARCH_TERM = "Esso";
    public static final String SEARCH_TERM_NOT_FOUND = "NO_PE";

    public static class WriteExampleApp {

        public static final Long ASSIGNEE_ID = 1L;
        public static final Long CLOSER_ID = 1L;
        public static final ZonedDateTime CREATION_TIME = parseDateTime("2016-12-03T13:41:28+02:00");
        public static final Long CREATOR_ID = 1L;
        public static final String DESCRIPTION = "This example contains integration tests";
        public static final Long ID = 1L;
        public static final ZonedDateTime MODIFICATION_TIME = parseDateTime("2016-12-03T13:41:28+02:00");
        public static final TaskResolution RESOLUTION = TaskResolution.DONE;
        public static final TaskStatus STATUS = TaskStatus.CLOSED;
        public static final String TITLE = "Write example application";
    }

    public static class WriteLesson {

        public static final ZonedDateTime CREATION_TIME =  parseDateTime("2016-12-04T13:41:28+02:00");
        public static final Long CREATOR_ID = 1L;
        public static final String DESCRIPTION = "This lesson talks about integration testing";
        public static final Long ID = 2L;
        public static final ZonedDateTime MODIFICATION_TIME =  parseDateTime("2016-12-04T13:41:28+02:00");
        public static final TaskStatus STATUS = TaskStatus.OPEN;
        public static final String TITLE = "Write lesson";

    }

    private static ZonedDateTime parseDateTime(String dateTime) {
        return ZonedDateTime.from(DATE_TIME_FORMAT.parse(dateTime));
    }
}
