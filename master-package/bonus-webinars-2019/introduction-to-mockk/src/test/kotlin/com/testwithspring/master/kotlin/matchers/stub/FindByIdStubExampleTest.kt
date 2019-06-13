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
@DisplayName("Stub function which has one simple function parameter")
class FindByIdStubExampleTest {

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
    fun shouldMatchSimpleFunctionArgumentWithDefaultMatcher() {
        every { repository.findById(ID) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }

    @Test
    @DisplayName("Ensures that the function argument is equal to the provided value")
    fun shouldMatchFunctionArgumentWithEq() {
        every { repository.findById(eq(ID)) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }

    @Test
    @DisplayName("Ensures that the function argument is not equal to the provided value")
    fun shouldMatchFunctionArgumentWithNeq() {
        every { repository.findById(neq(-1L)) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }

    @Test
    @DisplayName("Ensures that the function argument is less than the provided value")
    fun shouldMatchFunctionArgumentWithLess() {
        every { repository.findById(less(ID + 1)) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }

    @Test
    @DisplayName("Ensures that the function argument is less than or equal to the provided value")
    fun shouldMatchFunctionArgumentWithLessAndEqual() {
        every { repository.findById(less(value = ID, andEquals = true)) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }

    @Test
    @DisplayName("Ensures that the function argument is greater than the provided value")
    fun shouldMatchFunctionArgumentWithMore() {
        every { repository.findById(more(ID - 1)) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }

    @Test
    @DisplayName("Ensures that the function argument is greater than or equal to the provided value")
    fun shouldMatchFunctionArgumentWithMoreAndEqual() {
        every { repository.findById(more(value = ID, andEquals = true)) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }

    @Test
    @DisplayName("Ensures that any object is given as a function argument")
    fun shouldMatchFunctionArgumentWithAny() {
        every { repository.findById(any()) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }

    @Test
    @DisplayName("Ensures that a Long object is given as a function argument")
    fun shouldMatchFunctionArgumentWithOfType() {
        every { repository.findById(ofType(Long::class)) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }

    @Test
    @DisplayName("Ensures that the function argument fulfills the given predicate")
    fun shouldMatchFunctionArgumentWithMatch() {
        every { repository.findById(match { it == ID }) } returns TASK

        val returned = repository.findById(ID)
        assertThat(returned?.id).isEqualByComparingTo(ID)
    }
}