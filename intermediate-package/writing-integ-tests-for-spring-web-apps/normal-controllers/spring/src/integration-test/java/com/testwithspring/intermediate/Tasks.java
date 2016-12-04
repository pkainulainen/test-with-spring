package com.testwithspring.intermediate;

import com.testwithspring.intermediate.task.TaskResolution;
import com.testwithspring.intermediate.task.TaskStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class Tasks {

    private Tasks() {}

    public static final Long TASK_ID_NOT_FOUND = 599L;

    public static class WriteExampleApp {

        public static final Long ASSIGNEE_ID = 1L;
        public static final Long CLOSER_ID = 1L;
        public static final ZonedDateTime CREATION_TIME = ZonedDateTime.of(2016, 12, 3, 11, 41, 28, 0, ZoneId.systemDefault());
        public static final Long CREATOR_ID = 1L;
        public static final String DESCRIPTION = "This example contains integration tests";
        public static final Long ID = 1L;
        public static final ZonedDateTime MODIFICATION_TIME = ZonedDateTime.of(2016, 12, 3, 11, 41, 28, 0,  ZoneId.systemDefault());
        public static final TaskResolution RESOLUTION = TaskResolution.DONE;
        public static final TaskStatus STATUS = TaskStatus.CLOSED;
        public static final String TITLE = "Write example application";
    }

    public static class WriteLesson {

        public static final ZonedDateTime CREATION_TIME = ZonedDateTime.of(2016, 12, 4, 11, 41, 28, 0,  ZoneId.systemDefault());
        public static final Long CREATOR_ID = 1L;
        public static final String DESCRIPTION = "This lesson talks about integration testing";
        public static final Long ID = 2L;
        public static final ZonedDateTime MODIFICATION_TIME = ZonedDateTime.of(2016, 12, 4, 11, 41, 28, 0,  ZoneId.systemDefault());
        public static final TaskStatus STATUS = TaskStatus.OPEN;
        public static final String TITLE = "Write lesson";

    }
}
