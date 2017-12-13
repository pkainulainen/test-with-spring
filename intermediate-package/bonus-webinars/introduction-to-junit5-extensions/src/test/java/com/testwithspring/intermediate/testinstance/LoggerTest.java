package com.testwithspring.intermediate.testinstance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;

@ExtendWith(LoggerInjectorExtension.class)
class LoggerTest {

    private Logger logger;

    @Test
    void shouldWriteLineToLog() {
        logger.info("Hello logger");
    }
}
