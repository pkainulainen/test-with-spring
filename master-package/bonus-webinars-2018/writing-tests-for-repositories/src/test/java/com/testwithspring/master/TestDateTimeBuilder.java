package com.testwithspring.master;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Provides static factory methods that construct date and time
 * related test data. We will use this test data when we write
 * unit and integration tests for our web application.
 */
public final class TestDateTimeBuilder {

    private static final DateTimeFormatter LOCAL_DATE_TIME_FORMAT = DateTimeFormatter.ISO_DATE_TIME;

    private TestDateTimeBuilder() {}

    /**
     * Transforms the given UTC datetime to a local datetime and creates a new {@link ZonedDateTime} object by
     * using the local datetime.
     * @param dateTime  The UTC datetime string that uses the ISO date and time format without an offset.
     * @return  The created {@ZonedDateTime} object. Note that the returned object
     *          is created by using the system's default offset and zone ID.
     */
    public static ZonedDateTime parseLocalDateTimeFromUTCDateTime(String dateTime) {
        return transformUTCStringToZonedDateTime(dateTime);
    }

    private static ZonedDateTime transformUTCStringToZonedDateTime(String dateTime) {
        LocalDateTime utcDateTime = LocalDateTime.from(LOCAL_DATE_TIME_FORMAT.parse(dateTime));
        ZonedDateTime utcZonedDateTime = utcDateTime.atZone(ZoneId.of("UTC"));
        return utcZonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
    }
}
