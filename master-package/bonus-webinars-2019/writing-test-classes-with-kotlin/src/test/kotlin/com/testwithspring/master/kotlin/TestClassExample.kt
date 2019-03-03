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
@DisplayName("Demonstrates how we can write test classes with Kotlin and JUnit 5")
class TestClassExample {

    companion object {

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            println("Before all test functions")
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            println("After all test functions")
        }
    }

    @BeforeEach
    fun beforeEach() {
        println("Before each test function")
    }

    @AfterEach
    fun afterEach() {
        println("After each test function")
    }

    @Nested
    @DisplayName("Tests for the function A")
    inner class A {

        @BeforeEach
        fun beforeEach() {
            println("Before each test function of the A class")
        }

        @AfterEach
        fun afterEach() {
            println("After each test function of the A class")
        }

        @Test
        @DisplayName("Example test for function A")
        fun testForFunctionA() {
            println("Example test for function A")
        }

        @Nested
        @DisplayName("When X is true")
        inner class WhenX {

            @BeforeEach
            fun beforeEach() {
                println("Before each test function of the WhenX class")
            }

            @AfterEach
            fun afterEach() {
                println("After each test function of the WhenX class")
            }

            @Test
            @DisplayName("Example test for function A when X is true")
            fun testForFunctionAWhenX() {
                println("Example test for function A when X is true")
            }
        }
    }
}