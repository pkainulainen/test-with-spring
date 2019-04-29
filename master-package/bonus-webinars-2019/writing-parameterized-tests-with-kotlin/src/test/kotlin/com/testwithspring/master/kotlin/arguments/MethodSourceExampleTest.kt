package com.testwithspring.master.kotlin.arguments

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@Tag("unitTest")
@DisplayName("@MethodSource example")
class MethodSourceExampleTest {

    companion object {

        @JvmStatic
        private fun sumProvider(): Stream<Arguments> {
            return Stream.of(
                    Arguments.of(1, 1, 2),
                    Arguments.of(2, 3, 5)
            )
        }
    }

    @DisplayName("Should pass the function parameters provided by the sumProvider() function")
    @ParameterizedTest(name = "{index} => a={0}, b={1}, sum={2}")
    @MethodSource("sumProvider")
    fun sum(a: Int, b: Int, sum: Int) {
        assertThat(a + b).isEqualByComparingTo(sum)
    }
}