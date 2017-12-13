package com.testwithspring.intermediate.conditionaltest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ConditionalTestRunnerExtension.class)
@DisplayName("Run and ignore test methods")
class ConditionalTest {

    @Test
    @DisplayName("This test method should be run")
    void runThisTest() {
        System.out.println("This test should be run");
    }

    @Test
    @DisplayName("This test method should be ignored")
    void ignoreThisTest() {
        System.out.println("This test should be ignored");
    }
}
