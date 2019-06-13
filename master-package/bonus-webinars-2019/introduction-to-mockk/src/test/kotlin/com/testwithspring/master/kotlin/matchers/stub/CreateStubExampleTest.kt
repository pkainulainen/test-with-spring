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
@DisplayName("Stub function which has one complex function parameter")
class CreateStubExampleTest {

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
    @DisplayName("Ensures that the function argument has the correct id")
    fun shouldMatchFunctionArgumentWithEq() {
        every { repository.create(match { it.id == ID }) } returns TASK

        val returned = repository.create(TASK)
        assertThat(returned).isSameAs(TASK)
    }
}