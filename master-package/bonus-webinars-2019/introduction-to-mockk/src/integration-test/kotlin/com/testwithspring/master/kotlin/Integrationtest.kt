package com.testwithspring.master.kotlin

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("integrationTest")
class Integrationtest {

    @Test
    @DisplayName("Should be when we run our integration tests")
    fun shouldBeRunWhenWeRunOurIntegrationTests() {
        println("Should be run when we run our integration tests")
    }
}