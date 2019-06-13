package com.testwithspring.master.kotlin.matchers.mock

import com.testwithspring.master.kotlin.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unitTest")
@DisplayName("Verify interactions when the function has two simple function parameters")
class FindByCreatorAndStatusMockExampleTest {

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
    fun createRepositoryMock() {
        repository = mockk<TaskRepository>(relaxed = true)
    }

    @Test
    @DisplayName("By default, simple function arguments are matched with eq()")
    fun shouldMatchSimpleFunctionArgumentsWithDefaultMatcher() {
        repository.findByCreatorAndStatus(CREATOR_ID, STATUS)

        verify { repository.findByCreatorAndStatus(CREATOR_ID, STATUS) }
    }

    @Test
    @DisplayName("Ensures that the function arguments are equal to the provided values")
    fun shouldMatchFunctionArgumentsWithEq() {
        repository.findByCreatorAndStatus(CREATOR_ID, STATUS)

        verify { repository.findByCreatorAndStatus(eq(CREATOR_ID), eq(STATUS)) }
    }

    @Test
    @DisplayName("Ensures that we can mix regular arguments and argument matchers")
    fun shouldMixRegularArgumentAndArgumentMatcher() {
        repository.findByCreatorAndStatus(CREATOR_ID, STATUS)

        verify { repository.findByCreatorAndStatus(CREATOR_ID, eq(STATUS)) }
    }
}