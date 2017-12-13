package com.testwithspring.intermediate.lifecycle;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LifecycleLoggerExtension.class)
@DisplayName("Demonstrate how lifecycle callback extension works")
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
    @DisplayName("Sample test on root class")
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

    @Nested
    @DisplayName("Tests for the method A")
    class A {

        @BeforeEach
        void beforeEach() {
            System.out.println("Before each test method of the A class");
        }

        @AfterEach
        void afterEach() {
            System.out.println("After each test method of the A class");
        }

        @Test
        @DisplayName("Sample test for method A")
        void sampleTestForMethodA() {
            System.out.println("Sample test for method A");
        }
    }
}
