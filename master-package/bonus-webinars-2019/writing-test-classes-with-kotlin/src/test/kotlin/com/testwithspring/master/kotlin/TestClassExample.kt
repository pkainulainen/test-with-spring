package com.testwithspring.master.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested

@Tag("unitTest")
@DisplayName("Demonstrates how we can write test class with Kotlin and JUnit 5")
class TestClassExample {

    companion object {

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            println("Before all test methods")
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            println("After all test methods")
        }
    }

    @BeforeEach
    fun beforeEach() {
        println("Before each test method")
    }

    @AfterEach
    fun afterEach() {
        println("After each test method")
    }

    @Nested
    @DisplayName("Tests for the method A")
    inner class A {

        @BeforeEach
        fun beforeEach() {
            println("Before each test method of the A class")
        }

        @AfterEach
        fun afterEach() {
            println("After each test method of the A class")
        }

        @Test
        @DisplayName("Example test for method A")
        fun sampleTestForMethodA() {
            println("Example test for method A")
        }

        @Nested
        @DisplayName("When X is true")
        inner class WhenX {

            @BeforeEach
            fun beforeEach() {
                println("Before each test method of the WhenX class")
            }

            @AfterEach
            fun afterEach() {
                println("After each test method of the WhenX class")
            }

            @Test
            @DisplayName("Example test for method A when X is true")
            fun sampleTestForMethodAWhenX() {
                println("Example test for method A when X is true")
            }
        }
    }
}