package com.testwithspring.master.kotlin.matchers.mock

import com.testwithspring.master.kotlin.*
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unitTest")
@DisplayName("Verify interactions when the function has one complex function parameter")
class CreateMockExampleTest {

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
    @DisplayName("Ensures that the function argument has the correct id")
    fun shouldMatchFunctionArgumentWithEq() {
        repository.create(TASK)

        verify { repository.create(match { it.id == ID })}
    }
}