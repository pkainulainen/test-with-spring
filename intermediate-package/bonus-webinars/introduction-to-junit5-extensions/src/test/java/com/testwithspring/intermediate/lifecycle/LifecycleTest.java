package com.testwithspring.intermediate.lifecycle;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LifecycleLoggerExtension.class)
class LifecycleTest {

    @BeforeAll
    static void beforeAllonRootClass() {
        System.out.println("beforeAllOnRootClass()");
    }

    @BeforeEach
    void beforeEachOnRootClass() {
        System.out.println("beforeEachOnRootClass()");
    }

    @Test
    void testOnRootClass() {
        System.out.println("testOnRootClass()");
    }

    @AfterEach
    void afterEachOnRootClass() {
        System.out.println("afterEachOnRootClass()");
    }

    @AfterAll
    static void afterAllOnRootClass() {
        System.out.println("afterAllOnRootClass()");
    }
}
