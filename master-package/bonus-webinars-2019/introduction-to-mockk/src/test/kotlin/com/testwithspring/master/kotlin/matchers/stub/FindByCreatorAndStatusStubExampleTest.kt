package com.testwithspring.master.kotlin.matchers.stub

import com.testwithspring.master.kotlin.*
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unitTest")
@DisplayName("Stub functions which take one simple value as a function argument")
class FindByCreatorAndStatusStubExampleTest {

    companion object {

        private const val ID = 1L
        private const val CREATOR_ID = 5L
        private const val TITLE = "Write stub example"
        private val STATUS = TaskStatus.CLOSED
        private val RESOLUTION = TaskResolution.DONE

        private val TASK = Task(
                id = ID,
                creator = Creator(id = CREATOR_ID),
                title = TITLE,
                status = STATUS,
                resolution = RESOLUTION
        )
    }

    private lateinit var repository: TaskRepository

    @BeforeEach
    fun createRepositoryStub() {
        repository = mockk<TaskRepository>()
    }

    @Test
    @DisplayName("By default, simple function arguments are matched with eq()")
    fun shouldMatchSimpleFunctionArgumentsWithDefaultMatcher() {
        every { repository.findByCreatorAndStatus(CREATOR_ID, STATUS) } returns listOf(TASK)

        val returned = repository.findByCreatorAndStatus(CREATOR_ID, STATUS)
        assertThat(returned).hasSize(1)
    }

    @Test
    @DisplayName("Ensures that the function arguments are equal to the provided values")
    fun shouldMatchFunctionArgumentsWithEq() {
        every { repository.findByCreatorAndStatus(eq(CREATOR_ID), eq(STATUS)) } returns listOf(TASK)

        val returned = repository.findByCreatorAndStatus(CREATOR_ID, STATUS)
        assertThat(returned).hasSize(1)
    }

    @Test
    @DisplayName("Ensures that we can mix regular arguments and argument matchers")
    fun shouldMatchArgumentsWithDefaultMatcherAndProvidedMatcher() {
        every { repository.findByCreatorAndStatus(CREATOR_ID, eq(STATUS)) } returns listOf(TASK)

        val returned = repository.findByCreatorAndStatus(CREATOR_ID, STATUS)
        assertThat(returned).hasSize(1)
    }
}