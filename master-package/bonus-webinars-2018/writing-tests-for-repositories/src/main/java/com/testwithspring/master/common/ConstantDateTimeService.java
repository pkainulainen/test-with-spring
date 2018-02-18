package com.testwithspring.master.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is used in our integration tests and it always returns the
 * same time. This gives us the possibility to verify that the correct
 * timestamps are saved to the database.
 *
 * @author Petri Kainulainen
 */
public class ConstantDateTimeService implements DateTimeService {

    private static final DateTimeFormatter ZONED_DATE_TIME_FORMAT = DateTimeFormatter.ISO_ZONED_DATE_TIME;
    private static final DateTimeFormatter LOCAL_DATE_TIME_FORMAT = DateTimeFormatter.ISO_DATE_TIME;

    public static final String CURRENT_DATE_AND_TIME = getConstantDateAndTime();

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstantDateTimeService.class);

    private static String getConstantDateAndTime() {
        LocalDateTime utcDateTime = LocalDateTime.from(LOCAL_DATE_TIME_FORMAT.parse("2016-12-03T21:14:28"));
        ZonedDateTime utcZonedDateTime = utcDateTime.atZone(ZoneId.of("UTC"));
        return ZONED_DATE_TIME_FORMAT.format(utcZonedDateTime.withZoneSameInstant(ZoneId.systemDefault()));
    }

    @Override
    public ZonedDateTime getCurrentDateAndTime() {
        ZonedDateTime constantDateAndTime = ZonedDateTime.from(ZONED_DATE_TIME_FORMAT.parse(CURRENT_DATE_AND_TIME));

        LOGGER.info("Returning constant date and time: {}", constantDateAndTime);

        return constantDateAndTime;
    }
}
