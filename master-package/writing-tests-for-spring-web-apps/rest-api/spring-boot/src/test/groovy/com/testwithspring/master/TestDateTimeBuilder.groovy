package com.testwithspring.master

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Provides static factory methods that construct date and time
 * related test data. We will use this test data when we write
 * unit and integration tests for our web application.
 */
final class TestDateTimeBuilder {

    private static final DATE_TIME_FORMAT = DateTimeFormatter.ISO_ZONED_DATE_TIME

    /**
     * Creates a new {@link ZonedDateTime} object by using the date and time
     * String given as a method parameter.
     * @param dateTime  The date and time string that uses the ISO date and time format without an offset.
     * @return  The created {@ZonedDateTime} object. Note that the returned object
     *          is created by using the system's default offset and zone ID.
     */
    def static parseDateTime(String dateTime) {
        String dateTimeWithZone  = appendOffsetAndZoneIdToDateTime(dateTime)
        return ZonedDateTime.from(DATE_TIME_FORMAT.parse(dateTimeWithZone))
    }

    /**
     * Appends the system's default offset and zone ID to date and time string given as a method
     * parameter.
     * @param dateTime The date and time string that uses the ISO date and time format without an offset.
     * @return  The date and time string that contains both offset and zone ID.
     */
    def static appendOffsetAndZoneIdToDateTime(dateTime) {
        dateTime += getSystemZoneOffset()
        dateTime += getSystemZoneId()
        return dateTime
    }

    private static String getSystemZoneOffset() {
        return ZonedDateTime.now().getOffset().toString()
    }

    private static String getSystemZoneId() {
        return "[" + ZoneId.systemDefault().toString() + "]"
    }
}
