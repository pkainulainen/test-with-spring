package com.testwithspring.intermediate.conditionaltest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ConditionalTestRunnerExtension.class)
class ConditionalTest {

    @Test
    void runThisTest() {
        System.out.println("This test should be run");
    }

    @Test
    void ignoreThisTest() {
        System.out.println("This test should be ignored");
    }
}
