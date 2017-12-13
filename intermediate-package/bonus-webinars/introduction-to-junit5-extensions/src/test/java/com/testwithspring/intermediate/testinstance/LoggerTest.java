package com.testwithspring.intermediate.testinstance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;

@ExtendWith(LoggerInjectorExtension.class)
@DisplayName("Inject a Logger into our test object")
class LoggerTest {

    private Logger logger;

    @Test
    @DisplayName("Should write line to log")
    void shouldWriteLineToLog() {
        logger.info("Hello logger");
    }
}
