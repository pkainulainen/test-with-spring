package com.testwithspring.intermediate;

import org.junit.jupiter.api.*;

@DisplayName("JUnit 5 Nested Example")
class JUnit5NestedExampleTest {

    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all test methods");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("Before each test method");
    }

    @AfterEach
    void afterEach() {
        System.out.println("After each test method");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("After all test methods");
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

        @Nested
        @DisplayName("When X is true")
        class WhenX {

            @BeforeEach
            void beforeEach() {
                System.out.println("Before each test method of the WhenX class");
            }

            @AfterEach
            void afterEach() {
                System.out.println("After each test method of the WhenX class");
            }

            @Test
            @DisplayName("Sample test for method A when X is true")
            void sampleTestForMethodAWhenX() {
                System.out.println("Sample test for method A when X is true");
            }
        }
    }
}
