package com.testwithspring.master.kotlin.stubbing

import com.testwithspring.master.kotlin.*
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.lang.RuntimeException

@Tag("unitTest")
@DisplayName("Demonstrates how we can stub methods with MockK")
class TaskRepositoryStubTest {

    companion object {

        private const val CREATOR_ID = 5L
        private const val TASK_ID = 1L
        private const val TITLE = "Example task"
    }

    private lateinit var repository: TaskRepository

    @BeforeEach
    fun createStrictStub() {
        repository = mockk()
    }

    @Test
    @DisplayName("Should return the specified object")
    fun shouldReturnSpecifiedObject() {
        every { repository.findById(TASK_ID) } returns Task(
                id = TASK_ID,
                creator = Creator(id = CREATOR_ID),
                title = TITLE,
                status = TaskStatus.OPEN,
                resolution = null
        )

        val found = repository.findById(TASK_ID)

        val assertions = SoftAssertions()

        assertions.assertThat(found?.id).`as`("id").isEqualByComparingTo(TASK_ID)
        assertions.assertThat(found?.creator?.id).`as`("creator id").isEqualByComparingTo(CREATOR_ID)
        assertions.assertThat(found?.title).`as`("title").isEqualTo(TITLE)
        assertions.assertThat(found?.status).`as`("status").isEqualTo(TaskStatus.OPEN)
        assertions.assertThat(found?.resolution).`as`("resolution").isNull()

        assertions.assertAll()
    }

    @Test
    @DisplayName("Should different object on different invocations")
    fun shouldReturnDifferentObjectsOnDifferentInvocations() {
        every { repository.findById(TASK_ID) } returnsMany listOf(
                Task(
                        id = TASK_ID,
                        creator = Creator(id = CREATOR_ID),
                        title = TITLE,
                        status = TaskStatus.OPEN,
                        resolution = null
                ),
                Task(
                        id = TASK_ID,
                        creator = Creator(id = CREATOR_ID),
                        title = TITLE,
                        status = TaskStatus.CLOSED,
                        resolution = TaskResolution.DONE
                )
        )

        val first = repository.findById(TASK_ID)

        val assertions = SoftAssertions()

        assertions.assertThat(first?.id)
                .`as`("The id of the first task")
                .isEqualByComparingTo(TASK_ID)
        assertions.assertThat(first?.creator?.id)
                .`as`("The creator id of the first task")
                .isEqualByComparingTo(CREATOR_ID)
        assertions.assertThat(first?.title)
                .`as`("The title of the first task")
                .isEqualTo(TITLE)
        assertions.assertThat(first?.status)
                .`as`("The status of the first task")
                .isEqualTo(TaskStatus.OPEN)
        assertions.assertThat(first?.resolution)
                .`as`("The resolution of the first task").isNull()

        val second = repository.findById(TASK_ID)

        assertions.assertThat(second?.id)
                .`as`("The id of the second task")
                .isEqualByComparingTo(TASK_ID)
        assertions.assertThat(second?.creator?.id)
                .`as`("The creator id of the second task")
                .isEqualByComparingTo(CREATOR_ID)
        assertions.assertThat(second?.title)
                .`as`("The title of the second task")
                .isEqualTo(TITLE)
        assertions.assertThat(second?.status)
                .`as`("The status of the second task")
                .isEqualTo(TaskStatus.CLOSED)
        assertions.assertThat(second?.resolution)
                .`as`("The resolution of the second task").isEqualTo(TaskResolution.DONE)

        assertions.assertAll()
    }

    @Test
    @DisplayName("Should return the configured answer")
    fun shouldReturnReturnConfiguredAnswer() {
        every { repository.create(any()) } answers {
            val input = this.firstArg<Task>()

            Task(
                    id = 1L,
                    creator = input.creator,
                    title = input.title,
                    status = input.status,
                    resolution = input.resolution
            )
        }

        val created = repository.create(Task(id = null,
                creator = Creator(CREATOR_ID),
                title = TITLE,
                status = TaskStatus.OPEN,
                resolution = null
        ))

        val assertions = SoftAssertions()

        assertions.assertThat(created.id).`as`("id").isEqualByComparingTo(1L)
        assertions.assertThat(created.creator.id).`as`("creator id").isEqualByComparingTo(CREATOR_ID)
        assertions.assertThat(created.title).`as`("title").isEqualTo(TITLE)
        assertions.assertThat(created.status).`as`("status").isEqualTo(TaskStatus.OPEN)
        assertions.assertThat(created.resolution).`as`("resolution").isNull()

        assertions.assertAll()
    }

    @Test
    @DisplayName("Should throw an exception")
    fun shouldThrowException() {
        every { repository.findById(TASK_ID) } throws RuntimeException("an exception")

        assertThatThrownBy { repository.findById(TASK_ID) }
                .isExactlyInstanceOf(RuntimeException::class.java)
    }
}