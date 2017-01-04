package com.testwithspring.intermediate.common;

import java.time.ZonedDateTime;

/**
 * Declares the method that is used to get the current date and time.
 */
public interface DateTimeService {

    /**
     * Returns the current date and time.
     * @return
     */
    ZonedDateTime getCurrentDateAndTime();
}
